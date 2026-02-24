# 📈 Stock Trading Web

> JSP/Servlet 기반 주식 매매 웹 애플리케이션  
> **개발 기간:** 2025.12.10 ~ 2025.12.15

---

## 📖 프로젝트 소개

**Stock Trading Web**은 Java Servlet/JSP 기반으로 구현한 주식 매매 시뮬레이션 웹 애플리케이션입니다.

기존 콘솔 기반 프로젝트  
👉 [stock-trading-jdbc](https://github.com/SuYun25/stock-trading-jdbc)  
를 웹 환경으로 확장한 프로젝트로, DAO/Service 구조를 유지하면서  
웹 MVC 구조와 세션 기반 인증을 적용하여 사용자 인터페이스를 추가하였습니다.

사용자는 로그인 후 계좌를 관리하고, 종목을 조회하여 매수/매도를 수행할 수 있으며,  
챗봇 기능을 통해 주요 기능을 안내받을 수 있습니다.

---

## 🛠 기술 스택

### Backend
- Java
- Servlet
- JDBC
- Oracle Database

### Frontend
- JSP
- HTML / CSS
- JavaScript

### Server & Tools
- Apache Tomcat
- Eclipse (Dynamic Web Project)
- Git / GitHub

---

## ✨ 주요 기능

### 👤 회원 및 인증
- 로그인 / 로그아웃
- 세션(Session) 기반 로그인 상태 유지
- 인증되지 않은 사용자의 접근 제한

### 💳 계좌 기능
- 계좌 개설
- 계좌 목록 조회
- 입금 기능
- 계좌 간 이체

### 📊 주식 거래
- 종목 목록 조회
- 주식 매수 / 매도
- 보유 종목 조회 (수량, 평균 단가)
- 주문 내역 조회

### 🤖 챗봇 기능
- 규칙 기반 응답 시스템
- 세션에 채팅 기록 저장
- 잔고, 계좌, 보유 종목, 거래 내역 등 안내 기능 제공

---

## 🧭 아키텍처 구조

### MVC 패턴 기반 설계

- **Controller**  
  요청을 받아 서비스 계층으로 전달하고 화면(View)을 결정  
  (`UserController`, `TradeController`, `AccountController`, `ChatController` 등)

- **Service**  
  비즈니스 로직 처리

- **DAO**  
  JDBC 기반 데이터베이스 접근 처리

- **View (JSP)**  
  서버 데이터를 화면에 렌더링

---

## 📂 프로젝트 구조

```
src/main/java
├── stock.controller
├── stock.service
├── stock.dao
├── stock.dto
└── stock.util

src/main/webapp
├── index.jsp
└── views
    ├── login / signup / home
    ├── account
    ├── trade
    ├── order
    └── chat
```


---

## 💾 데이터베이스 테이블

| 테이블 | 설명 |
|--------|------|
| USERS | 사용자 정보 |
| ACCOUNTS | 계좌 정보 |
| STOCKS | 종목 정보 |
| HOLDINGS | 보유 종목 |
| ORDERS | 주문/거래 내역 |

---

## 🚀 실행 방법

1. Oracle DB 실행 및 테이블 생성
2. DB 접속 정보 설정 (`DBUtil` 또는 `db.properties`)
3. Eclipse에서 프로젝트 Import
4. Apache Tomcat 서버에 프로젝트 Add 후 Start
5. 브라우저에서 접속  
   `http://localhost:8080/<context-path>/`

---

## 🔐 보안 참고

현재 프로젝트는 학습용으로 제작되었으며,  
비밀번호 암호화 및 고급 보안 처리는 적용되어 있지 않습니다.

실서비스 환경에서는 비밀번호 해싱(BCrypt) 및 보안 강화를 반드시 적용해야 합니다.

---

## 🔄 프로젝트 확장 과정

본 프로젝트는 콘솔 기반 프로젝트에서 시작하여  
웹 MVC 구조로 확장한 단계적 학습 프로젝트입니다.

- 1단계: JDBC 기반 콘솔 주식 매매 시스템 구현  
- 2단계: Servlet/JSP 기반 웹 애플리케이션 확장  

이를 통해 동일한 비즈니스 로직을  
CLI 환경 → Web 환경으로 재구성하는 경험을 하였습니다.

---

## 📝 License

This project is for educational purposes only.

## 📂 프로젝트 구조
