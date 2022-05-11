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

<body>

<!--header start-->
<header class="header">
    <sec:authorize authority="ADMIN">
        <h2 > <a href="/main/admin/adminPanel" class="tbtn"  ><p  style="position: relative; top: -4px;">${lang.gL("ADMIN")}</p> </a></h2>
    </sec:authorize>
    <sec:authorize authority="Anonymous">
        <a href="/main/login"><img class="hbtn" src="../../static/img/login.png" alt="login"></a>
    </sec:authorize>
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
                    <h2>Our Rooms</h2>
                    <div class="bt-option">
                        <span>Rooms</span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- Breadcrumb Section End -->

<!-- Room Details Section Begin -->
<section class="room-details-section spad">
    <div class="container">
        <div class="row">
            <div class="col-lg-8">
                <div class="room-details-item">
                    <img src="/upload/room/${apartment.getImage()}" alt="img">
                    <div class="rd-text">
                        <div class="rd-title">
                            <h3>Premium King Room</h3>
                        </div>
                        <h2>${apartment.getPrice()} <span> ${lang.gL("pricePerNight")}</span></h2>
                        <table>
                            <tbody>
                            <tr>
                                <td class="r-o"  >${lang.gL("capacity")}</td>
                                <td>${apartment.getBeds()}</td>
                            </tr>
                            <tr>
                                <td class="r-o"> ${lang.gL("clazz")}  :</td>
                                <td>${apartment.getClazz()}</td>
                            </tr >
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <div class="col-lg-4">
                <div class="room-booking">
                    <h3>${lang.gL("yourReservation")}</h3>
                    <c:forEach var="item" items="${messages}">
                        <p> ${lang.gL(item)} </p>
                    </c:forEach>
                    <form action="apartment/book?apartment=${apartment.getId()}" method="post">
                        <div class="check-date">
                            <label for="date-in"  >${lang.gL("checkIn")}</label>
                            <input name = "arrivalDay" type="date" class="date-input" id="date-in" required>
                        </div>
                        <div class="check-date">
                            <label for="date-out" >${lang.gL("checkOut")}</label>
                            <input name = "endDay" type="date" class="date-input" id="date-out" required>
                        </div>
                        <h2>${lang.gL("weSendBillOnEmail")}</h2>
                        <button type="submit">${lang.gL("book")}</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</section>
<!-- Room Details Section End -->
</body>>
</html>