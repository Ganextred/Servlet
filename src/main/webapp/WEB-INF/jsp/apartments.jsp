<%@include file="/WEB-INF/jspf/generalSetings.jspf" %>
<%@ taglib uri="/WEB-INF/tld/sec.tld" prefix="sec" %>
<!DOCTYPE html>
<html lang="en">
<head>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>Rooms</title>

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
<div class="breadcrumb-section">
    <div class="container">
        <div class="row">
            <div class="col-lg-12">
                <div class="breadcrumb-text">
                    <h2>Our Rooms</h2>
                    <div class="bt-option">
                        <span >${lang.gL("Page")} : ${page}</span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!--Список комнат-->
<section class="rooms-section spad">
    <div class="container">
        <div>
            <p >${lang.gL("sort")}</p>
            <form action="apartments/applySort" method="post" id="applySort">
                <div>
                    <label> ${lang.gL("arrivalDay")}</label><input type = "date" name="arrivalDay" required value="${arrivalDayD}">
                    <label> ${lang.gL("endDay")}</label><input type = "date" name = "endDay"  value="${endDayD}" required>
                </div>
                <c:forEach var="i" begin="0" end="2">
                <label>
                    <select name = "sortParams[]" >
                        <option value="price"  ${sortParamsD[i] == 'price' ? 'selected' : ''} > ${lang.gL("price")} </option>
                        <option value="beds" ${sortParamsD[i] == 'beds' ? 'selected' : ''}  > ${lang.gL("beds")}</option>
                        <option value="clazz" ${sortParamsD[i] == 'clazz' ? 'selected' : ''} > ${lang.gL("clazz")} </option>
                    </select>
                    <select name = "orderParams[]" >
                        <option value="true"  ${orderParamsD[i] == 'true' ? 'selected' : ''} >${lang.gL("asc")}</option>
                        <option value="false" ${orderParamsD[i] == 'false' ? 'selected' : ''} > ${lang.gL("desc")}</option>
                    </select>
                </label>
                </c:forEach>
                <div>
                    <label>${lang.gL("showAvailable")}</label><input name = "AVAILABLE" type = "checkbox" <c:if test="${AVAILABLE}">checked</c:if> >
                    <label>${lang.gL("showBooked")}</label><input name = "BOOKED" type = "checkbox"   <c:if test="${BOOKED}">checked</c:if> >
                    <label>${lang.gL("showBought")}</label><input name = "BOUGHT" type = "checkbox"   <c:if test="${BOUGHT}">checked</c:if> >
                    <label>${lang.gL("showInaccessible")}</label><input name = "INACCESSIBLE" type = "checkbox"  <c:if test="${INACCESSIBLE}">checked</c:if>>
                </div>
                <button  type="submit">${lang.gL("confirm")}"</button>
            </form>
        </div>

        <div class="row">
            <c:forEach var="apartment" items="${apartments}">
            <div class="col-lg-4 col-md-6">
                <div class="room-item">
                    <img src="/upload/room/${apartment.getImage()}" alt="img1">
                    <div class="ri-text">
                        <h4>Premium King Room</h4>
                        <h3> ${apartment.getPrice()} <span>${lang.gL("pricePerNight")}</span></h3>
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
                        <c:url value="/main/apartment" var="apartmentURL">
                            <c:param name="apartment" value="${apartment.getId()}"/>
                        </c:url>
                        <a href="${apartmentURL}" class="primary-btn">${lang.gL("moreDetails")}</a>
                    </div>
                </div>
            </div>
            </c:forEach>

            <div class="col-lg-12">
                <div class="room-pagination">
                    <c:if test="${page > 1}">
                    <button type="submit" name="page" value="${page - 1}" form="applySort"> ${lang.gL("prevPage")} </button>
                    </c:if>
                    <button  type="submit"  name="page" value="${page + 1}" form="applySort">${lang.gL("nextPage")}</button>
                </div>
            </div>
        </div>
    </div>
</section>
</body>
</html>
