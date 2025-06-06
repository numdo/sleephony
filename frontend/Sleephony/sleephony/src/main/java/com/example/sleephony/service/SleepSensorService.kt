package com.example.sleephony.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.*
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.PowerManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.sleephony.R
import com.google.android.gms.wearable.Wearable
import com.samsung.android.service.health.tracking.ConnectionListener
import com.samsung.android.service.health.tracking.HealthTrackerException
import com.samsung.android.service.health.tracking.HealthTrackingService
import com.samsung.android.service.health.tracking.data.DataPoint
import com.samsung.android.service.health.tracking.data.HealthTrackerType
import com.samsung.android.service.health.tracking.data.ValueKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.json.JSONArray
import org.json.JSONObject
import java.util.Locale

class SleepSensorService : Service(), SensorEventListener {

    private var accelerometerList = JSONArray()
    private var accelerometerCnt = 0
    private val accelerometerTargetCnt = 3000

    private var heartRateList = JSONArray()
    private var heartRateCnt = 0
    private val heartRateTargetCnt = 150

    private var temperatureList = JSONArray()
    private var Temptemperature = "0"
    private var temperatureCnt = 0
    private val temperatureTargetCnt = 150

    private var lock = Any()

    private val serviceScope = CoroutineScope(Dispatchers.IO)
    private lateinit var sensorManager: SensorManager

    private var heathTrackingService: HealthTrackingService? = null
    private var skinTemperatureTacker: com.samsung.android.service.health.tracking.HealthTracker? = null
    private var skinTempHandler: Handler? = null
    private var isSkinTempAvailable = false

    private var wakeLock: PowerManager.WakeLock? = null
    private var isCollecting = true

    override fun onCreate() {
        super.onCreate()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "SleepSensorService::WakeLock")
        wakeLock?.acquire()

        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI)
        }

        val heartRate = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)
        if (heartRate != null) {
            sensorManager.registerListener(this, heartRate, SensorManager.SENSOR_DELAY_NORMAL)
        }
        connectHeatlthService()
        startForegroundServiceWithNotification()
    }

    private fun startForegroundServiceWithNotification() {
        val channelId = "sensor_service_channel"
        val channelName = "Sensor Service"
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)

        val notification: Notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("수면 센서 활성화됨")
            .setContentText("센서 데이터를 수집 중입니다...")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        startForeground(1, notification)
    }

    fun connectHeatlthService() {
        try {
            val connectionListener = object : ConnectionListener {
                override fun onConnectionSuccess() {
                    Log.d("ssafy","connected to Samsung Health Service")
                    heathTrackingService?.let { service->
                        val capability = service.trackingCapability
                        val availableTrackers = capability?.supportHealthTrackerTypes

                        isSkinTempAvailable = availableTrackers?.contains(HealthTrackerType.SKIN_TEMPERATURE_ON_DEMAND) ?: false

                        if (isSkinTempAvailable) {
                            initSkinTemperatureTracker()
                            Log.d("ssafy","skin temperature sensor is available")
                        } else {
                            Log.d("ssafy","skin temperature is not available")
                        }
                    }
                }

                override fun onConnectionEnded() {
                    Log.d("ssafy","Disconnected from Samsung Health Service")
                }

                override fun onConnectionFailed(p0: HealthTrackerException?) {
                    Log.e("ssafy","disconnected from samsung health service")
                }
            }
            heathTrackingService = HealthTrackingService(connectionListener, applicationContext)
            heathTrackingService?.connectService()
        } catch (e : Exception) {
            Log.e("ssafy","health service error $e")
        }
    }

    private fun initSkinTemperatureTracker() {
        try {
            skinTemperatureTacker = heathTrackingService?.getHealthTracker(HealthTrackerType.SKIN_TEMPERATURE_ON_DEMAND)
            skinTempHandler = Handler(Looper.getMainLooper())

            val skinTempListener = object : com.samsung.android.service.health.tracking.HealthTracker.TrackerEventListener {
                override fun onDataReceived(dataPoints: List<DataPoint>) {
                    for (data in dataPoints) {
                        val status = data.getValue(ValueKey.SkinTemperatureSet.STATUS)
                        if (status == 0) {
                            val skinTemp = data.getValue(ValueKey.SkinTemperatureSet.OBJECT_TEMPERATURE)
                            val temperature = String.format(Locale.getDefault(),"%.1f",skinTemp)
                            Temptemperature = temperature
                            if (temperatureCnt < temperatureTargetCnt && isCollecting) {
                                synchronized(lock) {
                                    temperatureList.put(Temptemperature)
                                }
                            }
                        }
                    }
                }

                override fun onFlushCompleted() {
                    Log.d("ssafy","skin temperature flush completed")
                }

                override fun onError(e: com.samsung.android.service.health.tracking.HealthTracker.TrackerError) {
                    Log.e("ssafy","skin temperature tracker error ${e}")
                }
            }
            skinTempHandler?.post {
                skinTemperatureTacker?.setEventListener(skinTempListener)
            }
        } catch (e : Exception) {
            Log.e("ssafy","error ${e}")
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event ?: return
        when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                val values = event.values.joinToString(", ") { "%.6f".format(it) }
                if (accelerometerCnt < accelerometerTargetCnt && isCollecting) {
                    synchronized(lock) {
                        accelerometerList.put(values)
                        accelerometerCnt++
                        countTarget()
                    }
                }
            }
            Sensor.TYPE_HEART_RATE -> {
                val values = event.values.joinToString(", ") { "%.6f".format(it) }
                if (heartRateCnt < heartRateTargetCnt && isCollecting) {
                    synchronized(lock) {
                        heartRateList.put(values)
                        heartRateCnt++
                        temperatureList.put(Temptemperature)
                        temperatureCnt++
                    }
                }
            }
        }
    }
    private fun countTarget() {
        if (!isCollecting) return
        if (
            accelerometerCnt == accelerometerTargetCnt &&
            heartRateCnt == heartRateTargetCnt &&
            temperatureCnt == temperatureTargetCnt
        ) {
            serviceScope.launch {
                isCollecting = false
                sendSensorMessage()
                delay(30000)
                isCollecting = true
            }
        }
    }

    private suspend fun sendSensorMessage(){
        try {
            val nodeClient = Wearable.getNodeClient(this)
            val messageClient = Wearable.getMessageClient(this)

            Log.d("ssafy","" +
                    "accelerometer ${accelerometerList} " +
                    "hearRate ${heartRateList} " +
                    "skinTemperature $temperatureList"
            )

            val jsonData = JSONObject().apply {
                put("mode","senser")
                put("accelerometer",accelerometerList)
                put("hearRate",heartRateList)
                put("temperature",temperatureList)
            }
            val jsonString = jsonData.toString()

            val baos = java.io.ByteArrayOutputStream()
            val gzos = java.util.zip.GZIPOutputStream(baos)
            gzos.write(jsonString.toByteArray(Charsets.UTF_8))
            gzos.close()

            val nodes = nodeClient.connectedNodes.await()
            for (node in nodes) {
                messageClient.sendMessage(
                    node.id,
                    "/alarm",
                    baos.toByteArray()
                ).await()
            }
            synchronized(lock) {
                accelerometerCnt = 0
                accelerometerList = JSONArray()
                heartRateCnt = 0
                heartRateList = JSONArray()
                temperatureCnt = 0
                temperatureList = JSONArray()
            }

        } catch (e:Exception) {
            Log.d("ssafy","$e")
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()

        wakeLock?.release()
        wakeLock = null

        sensorManager.unregisterListener(this)
        serviceScope.cancel()

        skinTemperatureTacker?.setEventListener(null)
        skinTemperatureTacker = null

        // 핸들러 제거
        skinTempHandler?.removeCallbacksAndMessages(null)
        skinTempHandler = null

        // 헬스 트래킹 서비스 연결 해제
        heathTrackingService?.disconnectService()
        heathTrackingService = null
    }

    override fun onBind(intent: Intent?): IBinder? = null
}