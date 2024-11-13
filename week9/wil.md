# 9주차에서 내운 내용

## 컨트롤러

-클라이언트의 요청을 받고, 응답을 보내는 계층  
-DTO(Data Trasfer Object)를 사용하여 서비스 계층과 데이터를 주고받는다.

## 프로토콜과 HTTP

-보통 어플리케이션과 관련된 데이터는 body에 담는다.  
-HTTP 요청으로 보내는 데이터는 Request Body
-HTTP 응답으로 보내는 데이터는 Response Body에 담긴다.

## 컨트롤러 계층

API 서버는 json 데이터를 응답하는 경우가 많다  
@ResponseBody를 사용하면 메서드가 자바 객체를 반환했을 때,
객체를 json 데이터로 변환해서 response body에 담아 응답한다.  
보통은 편의를 위해 @RestController를 대신 사용  
컨트롤러 계층은 서비스 계층에 의존  
생성자 주입 방식으로 서비스 빈을 주입 받는다  
@RequestMapping을 이용하여 메서드가 처리할 요청 method, url을 지정  
TodoController는 모든 todo 관련 요청을 처리하는 클래스  
이때 API 설계에서 todo 관련 요청은 모두 /todo로 시작  
URL 앞 공통 URL은 클래스에서 지정하고, URL 뒤 상세 URl은 메서드에서 지정할 수 있음  
보통 편의를 위해 요청 Method 종류는 어노테이션으로 지정

## 할 일 생성

Todo Service 기능을 사용하여 할 일 생성 요청을 처리  
할 일 데이터를 생성하려면 content, user 데이터를 받아야 함  
Request body 데이터는 보통 json 형식으로 들어오며, 메서드 파라미터로 받을 수 있음  
@RequestBody를 사용하여 파라미터로 들어오는 json 데이터를 자바 객체로 변환하여 받을 수 있음  
데이터를 받는 자바 객체를 DTO라고 함

## 컨트롤러 테스트

API를 테스트하는 도구로 postman을 사용함