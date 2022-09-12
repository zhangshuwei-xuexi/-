/**
 * Created by Amy on 2018/8/7.
 */
var questionnaie;
$(function () {
    isLoginFun();
    header();
    $("#ctl01_lblUserName").text(getCookie('userName'));
    getProjectInfo();

});

function getQuestionnaireSuccess(res) {
    questionnaie=res.data;
    // alert(questionnaie[0].question_name)
}

function getQuestionnaire(projectId) {
    var url='/queryQuestionnaireByProjectId';
    var data={
        "id":projectId
    };
    commonAjaxPost(false,url,data,getQuestionnaireSuccess);
}

// 查看项目详细信息
function getProjectInfo() {
    var projectId = getCookie('projectId');

    getQuestionnaire(projectId);
    var url = '/queryProjectListById';
    var data = {
        "id": projectId
    };
    commonAjaxPost(true, url, data, getProjectInfoSuccess);

}

// 查看项目详细信息成功,返回的是对应id的Questionnaire
function removeQuestionnaire(i) {
    id=getCookie("currentQuestionnaireId"+i);//写个方法根据Id删除
    var data={
        "id":id,
    };
    var url='/deleteQuestionnaireById';

    commonAjaxPost(true,url,data,removeQuestionnaireSuccess)
}
function removeQuestionnaireSuccess(result) {
    if (result.code == '666') {
        layer.msg('删除问卷成功', {icon: 1});
        history.go(0);
    } else if (result.code == "333") {
        layer.msg(result.message, {icon: 2});
    } else {
        layer.msg(result.message, {icon: 2});
    }
}
function getProjectInfoSuccess(result) {
    // //console.log(result)
    if (result.code == "666") {
        var projectInfo = result.data[0];
        $("#projectNameSpan").text(projectInfo.project_name);
        $("#createTimeSpan").text(projectInfo.creation_date.replace(/-/g,'/'));
        $("#adminSpan").text(projectInfo.created_by);
        $("#projectContentSpan").text(projectInfo.project_content);

        var text = "";
        var index=-1;
            for(var i=0;i<questionnaie.length;i++){
                var current=questionnaie[i];
                // alert(current.question_name);
                var j=i+1;
                index=i;
                var id=current.id;
                setCookie("currentQuestionnaireId"+i,id);
                text += "<tr>";
                text += "    <td style=\"text-align: center;color: #d9534f\" colspan=\"1\">"+j+"</td>";
                text += "    <td style=\"text-align: center;color: #d9534f\" colspan=\"1\">"+current.question_name+"</td>";
                text += "    <td style=\"text-align: center;color: #d9534f\" colspan=\"1\">"+current.release_time+"</td>";
                text += "    <td style=\"text-align: center;color: #d9534f\" colspan=\"1\">"+current.question_stop+"</td>";
                text += "    <td style=\"text-align: center;color: #d9534f\" colspan=\"1\"><button onclick=removeQuestionnaire("+index+")>删除</button></td>";
                text += "    <td style=\"text-align: center;color: #d9534f\" colspan=\"1\"><button onclick=tiaozhuan("+index+")>修改</button></td>";
                text += "    <td style=\"text-align: center;color: #d9534f\" colspan=\"1\"><button onclick=fasong("+index+")>发送</button></td>";
                text += "</tr>";
            }

        $("#questTableBody").empty();
        $("#questTableBody").append(text)

    } else if (result.code == "333") {
        layer.msg(result.message, {icon: 2});
        setTimeout(function () {
            window.location.href = 'login.html';
        }, 1000)
    } else {
        layer.msg(result.message, {icon: 2})
    }
}
function tiaozhuan(index) {
    window.location.href='/pages/editQuestionnaire.html?i='+index;
}
function fasong(index) {
    window.location.href='/pages/sendQuestionnaire.html?i='+index;
}

//编辑问卷
function editQuest(id, name, content, endTime, creationDate, dataId) {
    var data = {
        "id": id
    };
    commonAjaxPost(true, '/selectQuestionnaireStatus', data, function (result) {
        // if (result.code == "666") {
        //     if (result.data != "5") {
        //         layer.msg('问卷已发布，不可修改', {icon: 2});
        //     } else if (result.data == "5") {
        //         deleteCookie("questionId");
        //         deleteCookie("questionName");
        //         deleteCookie("questionContent");
        //         deleteCookie("endTime");
        //         setCookie("questionId", id);
        //         setCookie("questionName", name);
        //         setCookie("questionContent", content);
        //         setCookie("endTime", endTime);
        //         setCookie("creationDate", creationDate);
        //         setCookie("dataId", dataId);
        //         window.location.href = 'editQuestionnaire.html'
        //     }
        // }
        if (result.code == "666") {
            // if (result.data == "1") {
            //     if ($("#operationAll" + m + n).children("a:first-child").text() == '开启') {
            //         judgeIfChangeStatus(m, n);
            //     }
            //     layer.msg('问卷运行中，不可修改', {icon: 2});
            // } else

            // if (result.data != "1") {
            commonAjaxPost(true, '/selectQuestSendStatus', {id: id}, function (result) {
                //发送过问卷
                if (result.code == "40003") {
                    setCookie("ifEditQuestType", "false");
                } else if (result.code == "666") {         //未发送过问卷
                    setCookie("ifEditQuestType", "true");
                }
            });
            deleteCookie("questionId");
            deleteCookie("questionName");
            deleteCookie("questionContent");
            deleteCookie("endTime");
            setCookie("questionId", id);
            setCookie("questionName", name);
            setCookie("questionContent", content);
            setCookie("endTime", endTime);
            setCookie("creationDate", creationDate);
            setCookie("dataId", dataId);
            window.location.href = 'editQuestionnaire.html'
            // }
        }

        else if (result.code == "333") {
            layer.msg(result.message, {icon: 2});
            setTimeout(function () {
                window.location.href = 'login.html';
            }, 1000)
        } else {
            layer.msg(result.message, {icon: 2})
        }
    });
}

