//获取tag数据
function initData(){
    var userid = sessionStorage.getItem("userid");
    alert(userid);
    $('#payTagField').load('/getAllTags/'+ userid);
}