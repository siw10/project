/**
 * 
 */
let index = {
    init: function () {
        // jQuery 사용
        
        
        $("#btn-update").on("click", () => {
            this.update();
        });
		
    },



     update: function () {
        let data = {
            username: $("#username").val(),
            password: $("#password").val(),
            email: $("#email").val(),
            name: $("#name").val(),
            role: $("#role").val(),
            userTel: $("#userTel").val(),
			            
        }

        $.ajax({
            type: "PUT",
            url: "/roleModify",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            dataType: "json"
        }).done(function (res) {
            alert(res.data);
            location.href = "javascript:location.reload()";
        }).fail(function (error) {
            alert("통신에 에러가 발생했습니다. 잠시 후 다시 시도해주세요.");
        });

    },
    

}


index.init();