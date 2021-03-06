같이가자 기능 LIST
====================

어플실행시
-------------

* 2초간 로딩화면 띄워줌
<img src =./pic/로딩화면.jpg width="20%" height="20%">


로그인
-------------------
* 로그인 화면 

* 구글 , 네이버, 페이스북
<img src =./pic/로그인화면.jpg width="20%" height="20%">


초기화면
-------------------
### 메뉴

* 로그아웃
* 마이페이지
  >* 참여중인 이벤트 목록
  >* 완료된 이벤트 목록
    >* 보상선택 -> 보상신청 
  >* 랭킹보기
 * 도움말 
 <img src =./pic/메인화면_메뉴.jpg width="20%" height="20%">
  
  
### 등록

  * 등록장소 정보 입력 화면
    * 이벤트명 입력
    * 기간제한 입력 (~언제까지/ 제한 없음 선택)
    * 보상 입력
    * 랭킹을 어떤식으로 선정할지 미리 정함
      >* 최다 방문자?
      >* 최단시간내 많이 방문한 사람(타임어택)
      <img src =./pic/등록.jpg width="20%" height="20%">
      <img src =./pic/등록_예시.jpg width="50%" height="50%">
  * 장소 추가
    * 장소명 입력
    * 장소 주소찾기 / 입력
    * 장소 사진등록 
    * 상세정보 입력
    * 설명 입력
    * 인증방식 선택 (각 기능별로 본인꺼 설명 넣을것 EXIF 양제 , QR효준)
      * 사진정보 EXIF
        >* (양제 설명 넣을것) 
      * 사진정보 사진속 text
        >* (기능추가)
      * 비콘
        >* (기능추가)
      * QR코드
        >* (효준 설명 넣을것)
      * GPS
        >* 사전 입력된 장소별 주소를 입력한다.
        >* 입력된 주소는 위도 경도 정보로 변환된다.
        >* 장소의 위도 경도 data를 DB에 저장한다.
        
	<img src =./pic/등록_장소추가.jpg width="20%" height="20%">
	<img src =./pic/등록_예시.jpg width="50%" height="50%">
  * 등록정보 입력이 완료되면 DB에 저장되고 메인화면에 나타나야함
    (이미지 참조)
   
### 이벤트 목록
 * [등록] 이후에 생긴 이벤트들을 화면에 띄워준다
 * 인기가 많은것들을 상단에 노출시킬지, 등록순서별로 노출시킬지,
 * 아니면 테마별로 이벤트 모아서 보여주는것도 괜찮을듯
   
   
   
       
이벤트 화면 구성
---------------------------

### 이벤트 화면 구성

* 화면 중앙에 장소1~장소n까지 띄워준다.
  >* 스크롤내리면서 쭈욱 볼수있도록 구현
  >* 특별한 요구사항 없는경우 [등록]에서 등록한 순서대로 화면에 띄워준다


<img src =./pic/이벤트A예시.jpg width="20%" height="20%">

* 화면 하단부에 지도화면
  >* 위치정보 사용 허가 알림
  >* 화면에 내 위치 보여줌 (빨간 마커)
  >* 화면에 이벤트A에 등록된 모든 장소들 위치를 보여줌 (녹색마커)
  >* 마커를 선택하면 해당 장소의 등록이름만 뜨도록한다
  >* 지도화면 좌하단부 구글맵 어플 연동, 길찾기 할수있게함 (클릭하면 해당 어플로)
  
  <img src =./pic/이벤트A예시_지도.jpg width="20%" height="20%">
  
  
* 화면 최하단 좌측 [랭킹] 버튼
  >* 등록시 정해진 방식대로 DB에서 가져와 랭킹보여줌
  >* Sorting에 대한 방법 추후 논의할것
  >* 추가적인 기능 구현할것
 
* 화면 최하단 우측 [참여] 버튼
  >*[참여] 버튼 클릭시 [참여중]으로 바뀐다
  >* 이후 초기화면에서 마이페이지 >> 참여중인 이벤트 >> 이벤트A 뜨는거 확인가능
 
       
    
### 이벤트 참여 / 인증

* [이벤트A]를 선택 -> 화면에 장소1부터 장소N까지 띄운다.
  * (이미지 참조)
  
* 장소1 이미지를 클릭하는 경우
  >* 화면에 인증방식 목록이 뜬다
  >* 해당 장소에 대한 인증 방식만을 선택가능하게 한다. (나머지 음영처리)
     * (이미지 참조)
     
<img src =./pic/인증메뉴선택.jpg width="20%" height="20%">
<img src =./pic/인증방식선택.jpg width="40%" height="40%">



* 해당 인증 버튼 클릭시 인증화면이 나온다.

  * 사진으로 인증
    * 카메라/앨범 권한 허용 알림
    * 하단에 GALLEY ,CAMERA 선택 버튼 존재
      >* GALLEY 선택시 갤러리에 들어가서 해당 사진을 선택하면 사진속 EXIF 정보를 통해 장소 data와 비교후 인증여부 판단
      >* [장소]의 data 정보와 일치할 경우 인증이 완료되고 인증완료 Toast 띄움
      >* 일치하지 않을 경우 인증실패 Toast창 띄움
      
      
      <img src =./pic/인증_카메라.jpg width="20%" height="20%">
  
  
  * QR코드 인증
    >* 화면에 QR코드 인식 창이 뜨고 , QR코드를 인식하면 DB에 있는 data와 비교
    >* [장소]의 data 정보와 일치할 경우 인증이 완료되고 인증완료 Toast 띄움
    >* 일치하지 않을 경우 인증실패 Toast창 띄움
    
    
    <img src =./pic/인증_QR코드.jpg width="50%" height="50%">
    
    
  * 비콘으로 인증 
    >* (공부좀 하고 수정할것)

  * GPS로 인증
    >* 화면 중앙부에 지도 띄워주고 지도에 내위치/[장소1] 의 위치 마커로 각각 표기됨
    >* 하단부에 내 위치정보 가져오기 /인증 버튼이 
		>* 버튼 클릭시 현재 본인의 위치정보(위도,경도)가 나타난다.
		>* DB에 저장된 장소1의 위치정보와 일치할경우 인증이 완료되고 인증완료Toast띄움
    >* 일치하지 않을경우 인증실패 Toast
    
    
    <img src =./pic/인증_GPS.jpg width="20%" height="20%">
    <img src =./pic/인증_GPS2.jpg width="20%" height="20%">
    
    
* 인증 성공시 해당 [장소1]의 사진 음영처리
  * (이미지 참조)
  <img src =./pic/장소_인증완료.jpg width="50%" height="50%">
  
* 해당 EVENT의 모든 장소 인증 완료시
  * 화면에 [모든 장소 인증 완료] Toast 띄워줌
  * 클릭시 자동으로 마이페이지 >> 완료된 이벤트 목록 >> Event1으로 이동
  * Event1 - 이벤트에 대한 등록자의 보상설명
  * 하단에 랭킹/ 보상받기 버튼 (클릭시 보상 get)
    
    
    
 DB구축 / 서버관리
 --------------------------------------------
 * (DB/서버 기본작업 마치고 나면 이부분 작성좀, 추후에 내가 다시 정리해서 업데이트 하겠음)
 
 
 생각중인 추가 기능
 ----------------------------------------------
 *  새로운 랭킹시스템 도입
 *  친구에게 추천 / 초대
 *  운영자 관점에서 Log data 활용방안 모색
 *  사진속 meta data를 더 활용하는 방안
 *  인증방식에 사진 머신러닝 
 *  동적 QR코드를 활용 아두이노 OLED 연동하여 장소별로 설치
  >* 시간마다 바뀌는 QR코드 -> 인증시 도용,공유가 

  
  
