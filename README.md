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
* POST로 데이터를 전달할 경우 전달하는 데이터는 HTTP 본문에 담긴다.
* 본문의 길이는 HTTP 헤더의 Content-Length의 값이다.

### 요구사항 4 - redirect 방식으로 이동
* [List of HTTP status codes](https://en.wikipedia.org/wiki/List_of_HTTP_status_codes, "List of HTTP status codes")
* [HTTP 302 status code](https://en.wikipedia.org/wiki/HTTP_302, "HTTP 302 status code")<br>
 302 리다이렉트의 의미는 요청한 리소스가 임시적으로 새로운 URL로 이동했음(Temporarily Moved)을 나타낸다.
  
  

### 요구사항 5 - cookie
* [HTTP 쿠키](https://developer.mozilla.org/ko/docs/Web/HTTP/Cookies, "HTTP 쿠키")
  
* 쿠키값은 어떻게 생성하나?<br>
  응답 헤더에 Set-cookie를 추가해주면 된다!!<br>
  
* 생성된 쿠키는 어디서 봐야 하는 것일까?<br>
참고 : https://ledgku.tistory.com/72

### 요구사항 6 - stylesheet 적용
* [Content-Type 이란?](https://developer.mozilla.org/ko/docs/Web/HTTP/Headers/Content-Type "Content-Type")

### 요구사항 7번까지 완료하면서...
* 방법이 맞던 틀리던 요구 사항대로 개발하려고 노력을 했습니다. 그 중 가장 생소하고 고민을 많이 한 부분이
요청과 응답을 주고 받는 과정이었습니다.<br>
* [HTTP 메시지(요청,응답 헤더 설명)](https://developer.mozilla.org/ko/docs/Web/HTTP/Messages#HTTP_%EC%9D%91%EB%8B%B5 "HTTP 메시지")을 보고 개념을 잡았습니다.
구글 신의 도움을 받아 힌트를 사용해 요구 사항을 진행할 수 있었는데요... 기능 중심으로 빠르게 구현해보려 한 거라 잘못된 게 얼마나 많을지 상상도 안됩니다.
* 하지만 래팩토링과 단위 테스트에 중점을 두어 수정을 하다 보면 재밌는 부분들이 많을 것 같아
기대가 됩니다!!
* 요구사항 개발 과정을 정리한 블로그는 포스팅되는 대로 이곳에 업데이트하겠습니다.

### heroku 서버에 배포 후
* 