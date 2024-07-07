# 개발환경
1. IDE: Eclipse STS(Spring Tool Suite)
2. Build: Springboot 3.3.1 / Maven / War 
3. Language: JAVA(JDK 17), Javascript, Html/css
4. DB: Oracle 11.0.2 XE
5. Front: JSP, Jquery, ajax, bootstrap, (추후 +vue.js)
6. Dependencies
- mybatis
- ojdbc6
- spring boot 내장 tomcat
- lombok 1.18.24
- jstl 1.2
- tomcat-embed-jasper

# Tables

## 1. board
CREATE TABLE board (
	boardno NUMBER(9) PRIMARY KEY,
	title VARCHAR2(50) NOT NULL,
	writer VARCHAR2(20) NOT NULL,
	content VARCHAR2(500) NOT NULL,
	createdate DATE DEFAULT SYSDATE,
	hit NUMBER(9) NOT NULL
);

## 1-1. AUTONUM 시퀀스 생성
CREATE SEQUENCE boardno_seq
START WITH 1
INCREMENT BY 1 MAXVALUE 1000000
CYCLE NOCACHE;




