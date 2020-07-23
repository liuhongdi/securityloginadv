function go_logout() {
    if (confirm('您确认要退出系统吗?')) {
        var logout_url= "/login/logout";

        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");

        $.ajax({
            "url":logout_url,
            "type":"post",
            datatype: "json",

            beforeSend: function(request) {
                request.setRequestHeader(csrfHeader, csrfToken); // 添加  CSRF Token
            },
            success:function(data){
                if (data.code == 0) {
                    //
                    alert('logout success:'+data.msg);
                    //go
                    window.location.href="/home/home";
                } else {
                    alert("failed:"+data.msg);
                }
            }

        });

    }
}