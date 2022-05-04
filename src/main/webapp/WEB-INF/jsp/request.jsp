<%@include file="/WEB-INF/jspf/generalSetings.jspf" %>
<%@ taglib uri="/WEB-INF/tld/sec.tld" prefix="sec" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta charset="utf-8">

    <script src="//code.jquery.com/jquery-1.11.1.min.js"></script>
    <!-- Google Font -->
    <link href="https://fonts.googleapis.com/css?family=Lora:400,700&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Cabin:400,500,600,700&display=swap" rel="stylesheet">

    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="../../static/css/request.css">
    <link rel="stylesheet" href="../../static/css/header.css">
    <title>Advanced Search Form</title>
</head>

<body>

<header class="header">
    <sec:authorize authority="ADMIN">
        <h2 > <a href="/main/admin/adminPanel" class="tbtn"  ><p style="position: relative; top: -4px;">${lang.gL("ADMIN")} </p> </a></h2>
    </sec:authorize>
    <sec:authorize authority="Anonymous">
        <a href="/main/login"><img class="hbtn" src="../../static/img/login.png" alt="login"></a>
    </sec:authorize>
    <sec:authorize authority="Authorized">
        <form  action="/main/logout" method="post"> <input type="image" class="hbtn"  src="../../static/img/logout.png" alt="logout"> </form>
    </sec:authorize>
    <div class="lang_buttons">
        <input type="image" onclick="swap_len()" class="hbtn" src="../../static/img/lang.png" alt="lang">
    </div>
    <script>
        function swap_len(){$.get('/main/lang',function (data){location.reload()})}
    </script>
    <sec:authorize authority="Authorized">
        <form  action="/main/account" method="get"> <input type="image" class="hbtn"  src="../../static/img/account.png" alt="account"> </form>
    </sec:authorize>
</header>

<div class="container" id="advanced-search-form">
    <c:forEach var="item" items="${messages}">
        <p class="err-message"> ${lang.gL(item)} </p>
    </c:forEach>
    <form action="sendRequest" method="post">
        <div class="form-group">
            <label for="date-in" ${lang.gL("checkIn")} ></label>
            <input name = "arrivalDay" type="date" class="date-input" id="date-in" required>
        </div>
        <div class="form-group">
            <label for="date-out" ${lang.gL("checkOut")}></label>
            <input name = "endDay" type="date" class="date-input" id="date-out" required>
        </div>
        <div class="form-group">
            <select name="clazz">
                <option name = "ECONOMY" value="ECONOMY"  >${lang.gL("ECONOMY")}</option>
                <option name = "STANDARD" value="STANDARD"  >${lang.gL("STANDARD")}</option>
                <option name = "LUX" value="LUX"  >${lang.gL("LUX")}</option>
            </select>
        </div>
        <div class="form-group">
            <label for="beds"> ${lang.gL("beds")} </label>
            <input type="number" class="form-control" placeholder="${lang.gL("beds")}" name="beds" id="beds" required>
        </div>
        <div class="form-group">
           <input type="text" class="big-text" placeholder="${lang.gL("YourWishes")}" name="wishes" required>
        </div>
        <div class="clearfix"></div>
        <button type="submit" class="btn btn-info btn-lg btn-responsive" id="search" >${lang.gL("send")}</button>
    </form>
</div>
</body>

</html>