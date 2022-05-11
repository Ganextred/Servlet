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
                    <c:forEach var="image" items="${apartment.getImages()}">
                    <img src="/upload/room/${image}" alt="img">
                    </c:forEach>
                    <div class="rd-text">
                        <div class="rd-title">
                            <h3>Premium King Room</h3>
                        </div>
                        <h2> ${apartment.getPrice()} <span>${lang.gL("pricePerNight")}</span></h2>
                        <table>
                            <tbody>
                            <tr>
                                <td class="r-o">${lang.gL("capacity")}</td>
                                <td > ${apartment.getBeds()}</td>
                            </tr>
                            <tr>
                                <td class="r-o">${lang.gL("clazz")} :</td>
                                <td > ${apartment.getClazz()} </td>
                            </tr >
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <div class="col-lg-4">
                <div class="room-booking">
                    <c:forEach var="item" items="${messages}">
                        <p> ${lang.gL(item)} </p>
                    </c:forEach>
                    <form action="/main/admin/editApartment/save/?apartment=${apartment.getId()}" method="post" enctype="multipart/form-data">
                        <p> ${lang.gL("price")}</p>
                        <label>
                            <input name = "price" type="number" value="${apartment.getPrice()}" required>
                        </label>
                        <p> ${lang.gL("beds")}</p>
                        <label>
                            <input name = "beds" type="number" value="${apartment.getBeds()}" required>
                        </label>
                        <p> ${lang.gL("choseImage")}</p>
                        <input type="file" accept="image/jpeg" name="image">
                        <label>
                            <select name = "clazz">
                                <option value="ECONOMY"   ${apartment.getClazz().toString().equals('ECONOMY') ? 'selected' : ''} ${apartment.getClazz().toString().equals('ECONOMY')}> ${lang.gL("ECONOMY")}</option>
                                <option value="STANDARD"   ${apartment.getClazz().toString().equals('STANDARD') ? 'selected' : ''} ${apartment.getClazz().toString().equals('STANDARD')}> ${lang.gL("STANDARD")}</option>
                                <option value="LUX"   ${apartment.getClazz().toString().equals('LUX') ? 'selected' : ''}> ${lang.gL("LUX")}</option>
                            </select>
                        </label>
                        <button type="submit"> ${lang.gL("save")} </button>
                    </form>
                </div>
            </div>
        </div>
        <form action="/main/admin/editApartment/delete?apartment=${apartment.getId()}" method="post" enctype="multipart/form-data">
            <button type="submit"> ${lang.gL("delete")}</button>
        </form>
    </div>
</section>
<!-- Room Details Section End -->
</body>>
</html>