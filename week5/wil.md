# 5주차 스터디에서 배운 내용

## 레포지토리 계층

DB와 소통하며 데이터를 조작하는 계층, 서비스 계층이 결정한 비즈니스 로직을 실제 DB에 적용한다.

데이터 조작은 크게 4가지 기능 위주로 구현(CRUD)

-생성(Create)  
-조회(Read)   
-수정(Update)  
-삭제(Delete)

이 기능은 JPA가 제공하는 기능을 이용하여 구현

## 엔티티 매니저

JPA는 application.yml 정보를 통해 Entity Manager를 생성  
엔티티 매니저는 우리 대신 DB와 직접 소통하는 객체

엔티티 매니저가 하는 일  
-새로 생성한 엔티티 객체를 DB에 추가  
-DB에서 조회한 데이터로 엔티티 객체 만들기  
-엔티티 객체에 대한 수정, 삭제를 DB에 반영하기

## 트랜잭션

JPA는 DB와 유사하게 트랜잭션 단위로 동작  
트랜잭션이 끝나면 모든 변경사항을 DB에 반영  
트랜잭션 중간에 에러가 발생하면 트랜잭션 범위 안의 모든 변경점을 되돌린다(롤백)

## 영속성 컨텍스트

DB에서 조회한 엔티티를 캐싱하는 공간  
JPA가 DB에 반영할, 엔티티의 모든 변경 사항을 보관하는 공간  
엔티티에 대한 변경 사항을 영속성 컨텍스트에 저장해 두었다가, 트랜잭션을 커밋하면 저장된 모든 변경점이 DB에 반영되도록 영속성 컨텍스트를 기반으로 한번에 SQL을 생성
