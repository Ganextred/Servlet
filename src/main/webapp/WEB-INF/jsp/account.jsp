<%@include file="/WEB-INF/jspf/generalSetings.jspf" %>
<%@ taglib uri="/WEB-INF/tld/sec.tld" prefix="sec" %>
<!DOCTYPE html>
<html lang="en">


<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>Room</title>

    <script src="//code.jquery.com/jquery-1.11.1.min.js"></script>

    <!-- Google Font -->
    <link href="https://fonts.googleapis.com/css?family=Lora:400,700&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Cabin:400,500,600,700&display=swap" rel="stylesheet">

    <!-- Css Styles -->
    <link rel="stylesheet" href="../../static/css/header.css">
    <link rel="stylesheet" href="../../static/css/sona/bootstrap.min.css" type="text/css">
    <link rel="stylesheet" href="../../static/css/sona/font-awesome.min.css" type="text/css">
    <link rel="stylesheet" href="../../static/css/sona/elegant-icons.css" type="text/css">
    <link rel="stylesheet" href="../../static/css/sona/faticon.css" type="text/css">
    <link rel="stylesheet" href="../../static/css/sona/owl.carousel.min.css" type="text/css">
    <link rel="stylesheet" href="../../static/css/sona/nice-select.css" type="text/css">
    <link rel="stylesheet" href="../../static/css/sona/jquery-ui.min.css" type="text/css">
    <link rel="stylesheet" href="../../static/css/sona/magnific-popup.css" type="text/css">
    <link rel="stylesheet" href="../../static/css/sona/slicknav.min.css" type="text/css">
    <link rel="stylesheet" href="../../static/css/sona/style.css" type="text/css">
</head>

<!--header start-->
<header class="header">
    <sec:authorize authority="ADMIN">
        <h2 > <a href="/main/admin/adminPanel" class="tbtn"  ><p  style="position: relative; top: -4px;">${lang.gL("ADMIN")}</p> </a></h2>
    </sec:authorize>
    <sec:authorize authority="Authorized">
    <div>
        <form  action="/main/logout" method="post"> <input type="image" class="hbtn"  src="../../static/img/logout.png" alt="logout"> </form>
    </div>
    </sec:authorize>
    <div class="lang_buttons">
        <input type="image" onclick="swap_len()" class="hbtn" src="../../static/img/lang.png" alt="lang">
        <script>
            function swap_len(){$.get('/main/lang',function (data){location.reload()})}
        </script>
    </div>
</header>
<!--header end-->

<!-- Inviting Begin -->
<section class="blog-details-hero set-bg" data-setbg="../static/img/blog-details.jpg">
    <div class="cont">
        <div class="row">
            <div class="col-lg-10 offset-lg-1">
                <div class="bd-hero-text">
                    <span>${lang.gL("welcome")}</span>
                    <a href="requestForm"><h2>${lang.gL("readyToRestLeaveARequest")}</h2></a>
                    <ul>
                        <span class="b-time">${lang.gL("goodRest")}<i class="icon_clock_alt"></i></span>
                        <span> ${user.getUsername()} <i class="icon_profile"></i> </span>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</section>
<!-- Inviting End -->



<!-- Blog Section Begin -->
<section class="blog-section blog-page spad">
    <div class="container">
        <div class="row">
            <div class="col-lg-4 col-md-6">
                <div class="blog-item set-bg" data-setbg="img/blog/blog-1.jpg">
                    <div class="bi-text">
                        <div class="book-list">
                            <c:forEach var="bStatus" items="${bStatuses}">
                            <div>
                                <h6 >${lang.gL("bookId")}</h6>
                                <i>${bStatus.getId()} </i>
                                <h6>${lang.gL("Username")}</h6>
                                <span>${bStatus.getUser().getUsername()} </span>
                                <h6>${lang.gL("payTimeLimit")}</h6>
                                <i> ${bStatus.getPayTimeLimit()} </i>
                                <a href="apartment?apartment=${bStatus.getApartmentId().getId()}" class="btn btn-black">${lang.gL("see")}</a>
                            </div>
                            </c:forEach>
                            <span class="b-tag"> ${lang.gL("books")}</span>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-lg-4 col-md-6">
                <div class="blog-item set-bg" data-setbg="img/blog/blog-2.jpg">
                    <div class="bi-text">
                        <div class="book-list">
                            <c:forEach var="request" items="${requests}">
                            <div>
                                <h6>${lang.gL("requestId")}</h6>
                                <i> ${request.getId()} </i>
                                <h6> ${lang.gL("apartmentId")} </h6>
                                <a href="apartment?apartment=${request.getAnswerStatus().getApartmentId().getId().toString()}" class="btn btn-black">${lang.gL("see")}</a>
                                <form action="/main/confirmRequest?request=${request.getId()}" method="post">
                                    <input type="hidden" value="${request.getId()}" name="status" >
                                    <button type="submit"> ${lang.gL("confirmStatus")} </button>
                                </form>
                            </div>
                            </c:forEach>
                            <span class="b-tag" > ${lang.gL("requests")} </span>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-lg-4 col-md-6">
                <div class="blog-item set-bg" data-setbg="img/blog/blog-3.jpg">
                    <div class="bi-text">
                        <div class="book-list">
                            <c:forEach var="bgStatus" items="${bgStatuses}">
                            <div>
                                <h6>${lang.gL("bookId")} </h6>
                                <i> ${bgStatus.getId()} </i>
                                <h6> ${lang.gL("arrivalDay")} </h6>
                                <span> ${bgStatus.getArrivalDay()} </span>
                                <h6> ${lang.gL("endDay")} </h6>
                                <i> ${bgStatus.getEndDay()} </i>
                                <a href="/main/apartment?apartment=${bgStatus.getApartmentId().getId()}" class="btn btn-black"> ${lang.gL("see")} </a>
                            </div>
                            </c:forEach>
                            <span class="b-tag" > ${lang.gL("yourRooms")} </span>
                        </div>
                    </div>
                </div>
            </div>

        </div>
    </div>
</section>
</body>
</html>

