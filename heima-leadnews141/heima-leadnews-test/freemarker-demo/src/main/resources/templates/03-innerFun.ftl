<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>inner Function</title>
</head>
<body>

<b>获得集合大小</b><br>

集合大小：${stus?size}
<hr>
<b>获得日期</b><br>

显示年月日: ${today?date}       <br>

显示时分秒：${today?time}<br>

显示日期+时间：${today?datetime}<br>

自定义格式化：  ${today?string("yyyy年MM月")}<br>
<hr>
<b>内建函数C</b><br>
没有C函数显示的数值：${point} <br>
有C函数显示的数值：${point?c}
<hr>
</body>
</html>