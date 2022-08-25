/**
 * 
 */
let index = {
    init: function () {
        // jQuery 사용
        
        
       $("#btn-update").on("click", () => {
            this.update();
        });
        
       $("#btn-modify").on("click", () => {
            this.modify();
        });
		
    },



     update: function () {
        let data = {
			pno:$("#pno").val(),
            conPrice: $("#conPrice").val(),
            dogNumPrice: $("#dogNumPrice").val(),
            dogSizePrice: $("#dogSizePrice").val(),
            familyPrice: $("#familyPrice").val(),
            idPrice: $("#idPrice").val(),
            peopleNumPrice: $("#peopleNumPrice").val(),
            pictureSizePrice: $("#pictureSizePrice").val(),
            spaPrice: $("#spaPrice").val(),
            swimPrice: $("#swimPrice").val()
			            
        }

        $.ajax({
            type: "PUT",
            url: "/priceModify",
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
    
     modify: function () {
        let data = {
			bno:$("#bno").text(),
			bookingDate:$("#bookingDate").val()
			            
        }

        $.ajax({
            type: "PUT",
            url: "/bookingUpdate",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            dataType: "json"
        }).done(function (res) {
            alert(res.data);
            location.href = "javascript:location.reload()";
        }).fail(function (error) {
            alert("통신에 에러가 발생했습니다. 잠시 후 다시 시도해주세요.");
        });

    }
    

}


index.init();