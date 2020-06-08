window.addEventListener('DOMContentLoaded', () => {
    getGuestbooks();
});

curStart = 0;

showLoadingText = (show) => {
    let loadingElement = document.querySelector(".loading");
    let containerElement = document.querySelector(".container");
    setVisibility(loadingElement, show);
    setVisibility(containerElement, !show);
}

getGuestbooks = (start = 0) => {

    showLoadingText(true);

    fetchData(`/guestbooks?start=${start}`, null, "GET", json => {
        if (start > 0 && json.list.length == 0) {
            getGuestbooks(0);
            return;
        }

        let isAdmin = document.querySelector("#isAdmin").dataset.isAdmin;
        console.log("isAdminTag: " + document.querySelector("#isAdmin"));
        console.log('isAdmin : ' + isAdmin);
        document.querySelector(".count").innerText = json.count
        let listHtml = json.list.reduce((accumulator, guestbook) => {
            accumulator +=
                `<li>
                <div>
                    아이디: ${guestbook.id}<br>
                    작성자: ${guestbook.name}<br>
                    내용: ${guestbook.content}<br>
                    작성일: ${guestbook.regdate}
                </div>
            `;

            if (isAdmin) {
                accumulator += ` <input 
                type="button" 
                onclick="deleteGuestbook(${guestbook.id})" 
                value="삭제">`
            }
            accumulator += "</li>"
            return accumulator;
        }, "");
        document.querySelector(".list").innerHTML = listHtml;


        let pagesHtml = json.pageStartList.reduce((accumulator, pageStart, currentIndex) => {
            accumulator +=
                `<td><a href="javascript:void(0);" 
                onclick="
                    getGuestbooks(${pageStart});
            ">페이지 ${currentIndex + 1}</a></td>`
            return accumulator;
        }, "")

        document.querySelector(".pages").innerHTML = pagesHtml;

        let welcomeH2Tag = document.querySelector(".welcome")
        if (Boolean(json.firstVisit)) {
            welcomeH2Tag.innerText = "처음 오셨군요. 환영합니다!";
        }
        else {
            welcomeH2Tag.innerText = "또 오셨군요. 환영합니다!";
        }
        curStart = start;
        showLoadingText(false);
    });
};
deleteGuestbook = (guest_id) => {
    showLoadingText(true);
    fetchData(`/guestbooks/${guest_id}`, null, "DELETE", json => {
        if (json.success) {
            getGuestbooks(curStart);
        }
        else {
            alert('삭제 실패!');
            showLoadingText(false);
        }
    })

}

addGuestbook = (submitTag) => {
    let submitForm = submitTag.form;
    let name = submitForm.name.value;
    let content = submitForm.content.value;
    if (!name || !content) {
        alert('이름 또는 내용을 입력해주세요!');
        return;
    }
    fetchData(
        "/guestbooks",
        { name, content },
        "POST",
        json => {
            if (json && json.id && json.name) {
                getGuestbooks();
            } else {
                alert("방명록 작성 실패!")
            }
        }
    )
    submitForm.reset();
    return false;
}



fetchData = (url, data, method, callback) => {
    let requestInfo = {
        method,
        headers: {
            'Content-Type': 'application/json'
        }
    };
    if (data) {
        requestInfo.body = JSON.stringify(data);
    }
    fetch(url, requestInfo)
        .then(res => res.json())
        .then(json => {
            callback(json)
        });
}

setVisibility = (element, visible) => {
    element.style.display = visible ? "block" : "none";
}