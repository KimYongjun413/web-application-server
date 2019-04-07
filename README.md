# 실습을 위한 개발 환경 세팅
* https://github.com/slipp/web-application-server 프로젝트를 자신의 계정으로 Fork한다. Github 우측 상단의 Fork 버튼을 클릭하면 자신의 계정으로 Fork된다.
* Fork한 프로젝트를 eclipse 또는 터미널에서 clone 한다.
* Fork한 프로젝트를 eclipse로 import한 후에 Maven 빌드 도구를 활용해 eclipse 프로젝트로 변환한다.(mvn eclipse:clean eclipse:eclipse)
* 빌드가 성공하면 반드시 refresh(fn + f5)를 실행해야 한다.

# 웹 서버 시작 및 테스트
* webserver.WebServer 는 사용자의 요청을 받아 RequestHandler에 작업을 위임하는 클래스이다.
* 사용자 요청에 대한 모든 처리는 RequestHandler 클래스의 run() 메서드가 담당한다.
* WebServer를 실행한 후 브라우저에서 http://localhost:8080으로 접속해 "Hello World" 메시지가 출력되는지 확인한다.

# 각 요구사항별 학습 내용 정리
* 구현 단계에서는 각 요구사항을 구현하는데 집중한다. 
* 구현을 완료한 후 구현 과정에서 새롭게 알게된 내용, 궁금한 내용을 기록한다.
* 각 요구사항을 구현하는 것이 중요한 것이 아니라 구현 과정을 통해 학습한 내용을 인식하는 것이 배움에 중요하다. 

### 요구사항 1 - http://localhost:8080/index.html로 접속시 응답
* 잊지말자 상대경로! ( ./ = 현재 디렉토리 참조, ../ = 현재 디렉토리의 부모 디렉토리 참조)
* The try-with-resources Statement ( try( ) { } )<br>InputStream, OutputStream 은 Closeable 인터페이스를 사용하고 있어서
    () 안에 생성하게되면 try가 끝나고 따로 메모리를 해제하지 않아도 해제가 된다!(자바7부터 들어있는 기능)<br>
    참고 : https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html
* InputStream을 BufferedReader로 바꾸는 법<br>
  BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));

### 요구사항 2 - get 방식으로 회원가입
* Map, HasMap<br>
  참고 : https://wikidocs.net/208
  https://onsil-thegreenhouse.github.io/programming/java/2018/02/22/java_tutorial_1-24/
* String [] url2 = url.split("?"); 은 왜 안될까??<br>
  정규표현식의 특수문자로 '?' 가 쓰이기 때문에 문자로써 인식을 시켜야 구분자로써 사용할 수 있다.
  이스케이프 처리를 하여 다음과 같이 하면 된다.<br> String [] url2 = url.split("\\?");

### 요구사항 3 - post 방식으로 회원가입
* POST 방식이란? - 
* POST로 데이터를 전달할 경우 전달하는 데이터는 HTTP 본문에 담긴다.
* 본문의 길이는 HTTP 헤더의 Content-Length의 값이다.

### 요구사항 4 - redirect 방식으로 이동
* 

### 요구사항 5 - cookie
* 

### 요구사항 6 - stylesheet 적용
* 

### heroku 서버에 배포 후
* 