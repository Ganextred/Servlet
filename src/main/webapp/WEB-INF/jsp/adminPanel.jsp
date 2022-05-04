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
    <link rel="stylesheet" href="../../static/css/user-edit.css" type="text/css">
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <script>
        $(document).ready(function(){
            $('[data-toggle="tooltip"]').tooltip();
        });
    </script>
</head>

<!--header start-->
<header class="header">
    <sec:authorize authority="Authorized">
            <form  action="/main/logout" method="post"> <input type="image" class="hbtn"  src="../../static/img/logout.png" alt="logout"> </form>
    </sec:authorize>
    <div class="lang_buttons">
        <input type="image" onclick="swap_len()" class="hbtn" src="../../static/img/lang.png" alt="lang">
        <script>
            function swap_len(){$.get('/main/lang',function (data){location.reload()})}
        </script>
    </div>
    <sec:authorize authority="Authorized">
            <form  action="/main/account" method="get"> <input type="image" class="hbtn"  src="../../static/img/account.png" alt="account"> </form>
    </sec:authorize>
</header>
<!--header end-->

<!-- Breadcrumb Section Begin -->
<div class="breadcrumb-section">
    <div class="container">
        <div class="row">
            <div class="col-lg-12">
                <div class="breadcrumb-text">
                    <h2>Blog</h2>
                    <div class="bt-option">
                        <span>Blog Grid</span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- Breadcrumb Section End -->



<!-- Blog Section Begin -->
<section class="blog-section blog-page spad">
    <div class="container">
        <div class="row">
            <div class="col-lg-4 col-md-6">
                <div class="blog-item set-bg" data-setbg="img/blog/blog-1.jpg">
                    <div class="bi-text">
                        <div class="book-list">
                            <c:forEach var="bStatus" items="${bStatuses}">
                            <div >
                                <h6 >${lang.gL("bookId")}</h6>
                                <i>${bStatus.getId()} </i>
                                <h6> ${lang.gL("bookType")}</h6>
                                <i> ${lang.gL(bStatus.getStatus())} </i>
                                <h6>${lang.gL("Username")}</h6>
                                <span>${bStatus.getUser().getUsername()} </span>
                                <h6>${lang.gL("payTimeLimit")}</h6>
                                <i> ${bStatus.getPayTimeLimit()} </i>
                                <c:if test="${bStatus.getStatus().name().equals('BOOKED')}">
                                <form action="confirmStatus" method="post">
                                    <input type="hidden" value="${bStatus.getId()}" name="status">
                                    <button type="submit"> ${lang.gL("confirmStatus")}</button>
                                </form>
                                </c:if>
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
                                <h6> ${lang.gL("requestId")} </h6>
                                <i> ${request.getId()} </i>
                                <a href="seeRequest?request=${request.getId()}" class="btn btn-black" >${lang.gL("see")}</a>
                            </div>
                            </c:forEach>
                            <span class="b-tag"> ${lang.gL("requests")} </span>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-lg-4 col-md-6">
                <div class="blog-item set-bg" data-setbg="img/blog/blog-3.jpg">
                    <div class="bi-text">
                        <div class="book-list">
                            <c:forEach var="apartment" items="${apartments}">
                            <div>
                                <h6> ${lang.gL("apartmentId")} </h6>
                                <span> ${apartment.getId()} </span>
                                <a href="editApartment?apartment=${apartment.getId()}" class="btn btn-black" > ${lang.gL("see")} </a>
                            </div>
                            </c:forEach>
                            <form action="newApartment" method="post">
                                <button type="submit"> ${lang.gL("newApartment")}</button>
                            </form>
                            <span class="b-tag" >${lang.gL("apartments")}</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>
</body>
</html>

