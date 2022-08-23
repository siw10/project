/**
 * 
 */
 //////예약 관련 함수들
var bType;
var requesthour = []; // key 값을 담을 배열
var alreadytime=[];

function choiceBType(value){
    bType=value;
}
function doDeleteReservation(id){
    var xhr;
    var url='/deletereservation'; 
    var data=JSON.stringify({"bno":""+id+""});
    var contentType="application/json";
    alert('취소중 입니다 잠시만 기다려 주세요');
    xhr=doajax(url,data,contentType);
    xhr.onload = function() { 
        if(xhr.status==200){ // success:function(data)부분 통신 성공시 200반환
            var result=JSON.parse(xhr.response);
            if(result.result){
                alert(result.messege);
                location.href='/showreservationpage';
            }else{
                alert(result.messege);
            }
        }
        else{
            alert('통신에 실패했습니다');
        }
    }   
}
function goUpdateReservation(id){
    location.href='/updatereservationpage?id='+id;
}
