'use strict';

$(function () {

    // author badge :)
    var author = '<div style="position: fixed;bottom: 0;right: 20px;background-color: #fff;box-shadow: 0 4px 8px rgba(0,0,0,.05);border-radius: 3px 3px 0 0;font-size: 12px;padding: 5px 10px;">By <a href="https://github.com/CNYuYang">@CNYuYang</a></div>';
    $("body").append(author);

    $("input[type='password'][data-eye]").each(function (i) {
        var $this = $(this),
            id = 'eye-password-' + i,
            el = $('#' + id);

        $this.wrap($("<div/>", {
            style: 'position:relative',
            id: id
        }));

        $this.css({
            paddingRight: 60
        });
        $this.after($("<div/>", {
            html: 'Show',
            class: 'btn btn-primary btn-sm',
            id: 'passeye-toggle-' + i,
        }).css({
            position: 'absolute',
            right: 10,
            top: ($this.outerHeight() / 2) - 12,
            padding: '2px 7px',
            fontSize: 12,
            cursor: 'pointer',
        }));

        $this.after($("<input/>", {
            type: 'hidden',
            id: 'passeye-' + i
        }));

        var invalid_feedback = $this.parent().parent().find('.invalid-feedback');

        if (invalid_feedback.length) {
            $this.after(invalid_feedback.clone());
        }

        $this.on("keyup paste", function () {
            $("#passeye-" + i).val($(this).val());
        });
        $("#passeye-toggle-" + i).on("click", function () {
            if ($this.hasClass("show")) {
                $this.attr('type', 'password');
                $this.removeClass("show");
                $(this).removeClass("btn-outline-primary");
            } else {
                $this.attr('type', 'text');
                $this.val($("#passeye-" + i).val());
                $this.addClass("show");
                $(this).addClass("btn-outline-primary");
            }
        });
    });

    $("#button_login").click(function (event) {
        var form = $(".my-login-validation");
        if (form[0].checkValidity() === false) {
            form.addClass('was-validated');
            return;
        }
        form.addClass('was-validated');
        const data = {};
        data.email = $("#email").val();
        data.password = $("#password").val();
        $.ajax({
            url: "/admin/login",
            data: JSON.stringify(data),
            type: "POST",
            contentType: "application/json",
            dataType: "json",
            success: function (data) {
                window.location.href = "/admin/home.html";
            },
            error: function () {
                $("#element").toast('show')
            }
        })
    });
});