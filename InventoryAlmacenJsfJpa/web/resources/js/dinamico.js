$(document).ready(function(){
   
    var links = $(".nav .nav-item a");
    
    $.each(links, function (key, va){
        if (va.href === document.URL) {             
            $(this).addClass('active');
        }
    });
   
    
});