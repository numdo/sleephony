# Sleephony 프로젝트

<p align="center"> 
  <img src="exec/assets/ic_sleephony_logo.png" width="50%">
</p>

## 프로젝트 개요

### 프로젝트 소개

Sleephony는 갤럭시 워치에서 수집한 생체 데이터를 바탕으로, 수면 중 사용자의 수면 단계를 분석하고 그 순간에 가장 적합한 ASMR 또는 소리를 자동으로 재생하여 더 깊고 건강한 수면을 유도하는 스마트 수면 파트너입니다.<br/>
그리고 수면 리포트와 AI 피드백을 제공하여 사용자의 수면 습관 개선을 돕고 있습니다.

### 타겟 사용자

- 수면 건강에 관심 많은 사용자
- 수면 패턴이 불규칙한 직장인 / 수험생
- 수면 장애나 피로감을 자주 느끼는 사용자자
- 웨어러블 기기를 활용한 건강 관리에 익숙한 사용자

### 주요기능

#### 💤수면 측정

- ASMR 테마 설정
- 알람 시간 및 알람 모드 설정
- 실시간 수면 측정 단계에 맞는 ASMR 및 사운드 재생

#### 🗒️수면 리포트

- 날짜별 수면 리포트 제공
- 해당 날짜의 수면 점수 및 수면 요약 정보 제공
- Chat GPT API를 활용한 수면 피드백 제공

#### 📊수면 통계

- 기간별 수면 통계 제공
- 해당 기간의 수면 점수 및 수면 요약 정보 제공
- 기간 수면 시간의 상세 정보 제공
  <br />
  <br />
  <br />

## 개발환경

### 프론트엔드

| 항목                | 기술                                                                                                                                 |
| ------------------- | ------------------------------------------------------------------------------------------------------------------------------------ |
| **개발 환경**       | ![Andorid Studio](https://img.shields.io/badge/Android_Studio-3DDC84.svg?&style=for-the-badge&logo=androidstudio&logoColor=white)    |
| **프로그래밍 언어** | ![Kotlin](https://img.shields.io/badge/kotlin-7F52FF.svg?&style=for-the-badge&logo=kotlin&logoColor=black)                           |
| **빌드 도구**       | ![Gradle](https://img.shields.io/badge/gradle-02303A.svg?&style=for-the-badge&logo=gradle&logoColor=white)                           |
| **UI 툴킷**         | ![Jetpack Compose](https://img.shields.io/badge/jetpack_compose-4285F4.svg?&style=for-the-badge&logo=jetpackcompose&logoColor=black) |
| **인증/보안**       | ![OAuth](https://img.shields.io/badge/OAuth-3A3A3A.svg?&style=for-the-badge&logo=Oauth&logoColor=white)                              |

### 백엔드

| 항목                | 기술                                                                                                                                                                                                                                                |
| ------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **프로그래밍 언어** | ![Java](https://img.shields.io/badge/Java-007396.svg?&style=for-the-badge&logo=OpenJDK&logoColor=white)                                                                                                                                             |
| **개발 환경**       | ![IntelliJ IDEA](https://img.shields.io/badge/IntelliJ_IDEA-000000.svg?&style=for-the-badge&logo=IntelliJ-IDEA&logoColor=white)                                                                                                                     |
| **빌드 도구**       | ![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?&style=for-the-badge&logo=Gradle&logoColor=white)                                                                                                                                          |
| **프레임워크**      | ![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F.svg?&style=for-the-badge&logo=Spring-Boot&logoColor=white)                                                                                                                           |
| **보안**            | ![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F.svg?&style=for-the-badge&logo=Spring-Security&logoColor=white) ![JWT](https://img.shields.io/badge/JWT-000000.svg?&style=for-the-badge&logo=JSON-Web-Tokens&logoColor=white) |

### 공통

| 항목             | 기술                                                                                                                                                                                                                                                                                                                             |
| ---------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **형상 관리**    | ![Git](https://img.shields.io/badge/Git-F05032.svg?&style=for-the-badge&logo=Git&logoColor=white) ![GitLab](https://img.shields.io/badge/GitLab-FC6D26.svg?&style=for-the-badge&logo=GitLab&logoColor=white)                                                                                                                     |
| **협업**         | ![Notion](https://img.shields.io/badge/Notion-000000.svg?&style=for-the-badge&logo=Notion&logoColor=white) ![JIRA](https://img.shields.io/badge/JIRA-0052CC.svg?&style=for-the-badge&logo=JIRA&logoColor=white)                                                                                                                  |
| **데이터베이스** | ![MySQL](https://img.shields.io/badge/MySQL-4479A1.svg?&style=for-the-badge&logo=MySQL&logoColor=white)                                                                                                                                                                                                                          |
| **커뮤니케이션** | ![Mattermost](https://img.shields.io/badge/Mattermost-0072C6.svg?&style=for-the-badge&logo=Mattermost&logoColor=white)                                                                                                                                                                                                           |
| **배포**         | ![Jenkins](https://img.shields.io/badge/Jenkins-D24939.svg?&style=for-the-badge&logo=Jenkins&logoColor=white) ![Docker](https://img.shields.io/badge/Docker-2496ED.svg?&style=for-the-badge&logo=Docker&logoColor=white) ![Nginx](https://img.shields.io/badge/Nginx-009639.svg?&style=for-the-badge&logo=Nginx&logoColor=white) |

## 설계

### 시스템 아키텍쳐

<details>
  <summary>시스템 아키텍쳐 보기</summary>
  <p align="center">
    <img src="exec/assets/system.png" width="75%" >
  </p>
</details>

### 화면 설계서

<details>
  <summary>화면 설계서 보기</summary>
  <p align="center">
    <img src="exec/assets/prototype.png" width="75%" >
  </p>
</details>

## 기능 상세

### 수면 측정

#### 테마 설정

<img src="exec/assets/function/음악테마선택2.gif" >

#### 알람 설정

<img src="exec/assets/function/알람시간설정.gif" >
<img src="exec/assets/function/알람종류선택.gif" >

#### 수면 측정

<img src="exec/assets/function/수면 측정2.gif" >

### 수면 리포트

#### 날짜별 리포트

<img src="exec/assets/function/날짜별리포트.gif" >

#### AI 리포트

<img src="exec/assets/function/AI수면리포트.gif" >

### 수면 통계

#### 기간별 수면 통계

<img src="exec/assets/function/수면통계.gif" >
<img src="exec/assets/function/수면통계-1개월.gif" >

### 기타 기능

#### 로그인

<img src="exec/assets/function/로그인.gif" >

#### 개인정보 수정

<img src="exec/assets/function/개인정보 수정.gif" >

#### 연동된 스마트워치 확인

<img src="exec/assets/function/스마트워치 연동.gif" >

#### 사용자 가이드

<img src="exec/assets/function/사용자 가이드.gif" >

## 팀원 소개

### 김지원

- 모바일 앱 개발
- 모바일 앱 구조 설정
- 알람 기능, 테마 설정 기능, 수면 측정 기능, 로그인 기능

### 김도현

- 백엔드 서버 개발
- 수면 측정 API, 수면 리포트 및 통계 API

### 김소연

- 인프라, CI/CD

### 김신우

- 데이터 전처리
- AI 모델 설계&학습
- FastAPI

### 박성원

- 모바일 앱 개발 및 백엔드 서버 개발
- 모바일 앱 수면 리포트 기능
- 백엔드 회원 API

### 전홍석

- 모바일 앱 개발, 웨어러블 앱 개발
- 모바일-웨어러블 연동
- 모바일 수면 통계 기능
