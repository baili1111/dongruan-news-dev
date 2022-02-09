<html>
<head>
    <title>Hello Freemarker</title>
</head>
<body>
    <#--
        Freemarker的构成语法：
        1. 注释
        2. 表达式
        3. 指令
        4. 普通文本
    -->

    <#-- 注释 -->
    <#-- ${} 为变量表达式，同jsp -->

    <#-- 输出字符串 -->
    <div>hello ${there}</div>

    <br>

    <#-- 输出对象 -->
    <div>
        用户id：${stu.uid}<br/>
        用户姓名：${stu.username}<br/>
        年龄：${stu.age}<br/>
        生日：${stu.birthday?string('yyyy-MM-dd HH:mm:ss')}<br/>
        余额：${stu.amount}<br/>
        已育：${stu.haveChild?string('yes', 'no')}<br/>
        伴侣：${stu.spouse.username}, ${stu.spouse.age}岁
    </div>

    <br>

    <#-- 输出list -->
    <#-- stu.articleList -->
    <div>
        <#list stu.articleList as article>
            <div>
                <span>${article.id}</span>
                <span>${article.title}</span>
            </div>
        </#list>
    </div>


    <br>

    <#-- 输出map -->
    <#-- stu.parents -->
    <div>
        <#list stu.parents?keys as key>
            <div>${stu.parents[key]}</div>
        </#list>
    </div>

    <br>

    <#-- if 指令判断 -->
    <div>
        <#if stu.uid == '1001'>
        用户id是1001
        </#if><br/>
        <#if stu.username != 'Jack'>
        用户姓名不是Jack
        </#if><br/>
        <#if (stu.age >= 18)>
        成年人
        </#if><br/>
        <#if (stu.age < 18)>
        未成年人
        </#if><br/>
        <#if stu.haveChild>
        已育
        </#if><br/>
        <#if !stu.haveChild>
        未育
        </#if><br/>
    </div>

    <br>

    <#if stu.spouse??>
        伴侣：${stu.spouse.username}, ${stu.spouse.age}岁
    </#if>
    <#if !stu.spouse??>
        单身狗
    </#if>



</body>
</html>
