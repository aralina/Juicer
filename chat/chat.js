/**
 * Created by home on 03.03.15.
 */
var user = "";
var uniqueId = function() {
    var date = Date.now();
    var random = Math.random() * Math.random();
    return Math.floor(date * random).toString();
};

var messageList = [];
function run() {
    var appContainer = document.getElementsByClassName('chat')[0];

    if (restore() != null) {
        var allMessages = restore();
        createAllMessages(allMessages);
    }

    if ((user === "")||(user === undefined))
        alert("Welcome, stranger");
    else alert("Welcome back, " + user);
    appContainer.addEventListener('click', delegateEvent);
    appContainer.addEventListener('change', delegateEvent);
}

function delegateEvent(evtObj) {
    if (evtObj.type === 'click' && evtObj.target.classList.contains('send')) {
        onSendMessageButton(evtObj);
    }
    if (evtObj.type === 'change' && evtObj.target.nodeName == 'INPUT') {
        var labelEl = evtObj.target.parentElement;
        setMarker(labelEl);
    }

    if (evtObj.type === 'click' && evtObj.target.classList.contains('delete')) {

        deleteMsg(evtObj);
    }
    if (evtObj.type === 'click' && evtObj.target.classList.contains('edit')) {
        editMsg(evtObj);
    }
    if (evtObj.type === 'click' && evtObj.target.classList.contains('allMessages')) {
        selectMsg(evtObj);
    }
}

function setMarker(labelEl) {
    if (labelEl.classList.contains('Check'))
        labelEl.classList.remove('Check');
else labelEl.classList.add('Check');
}

function setUsername() {
    user = prompt("Username","");
}

function changeUsername() {
    user = prompt("Username","");
}

function onSendMessageButton() {
    if(user === "") {
        alert("Input username!");
        return;
    }

    var text = document.getElementById('sendMessage');
    if (text.value == '')
    return;
    var newMessage = theMessage(text.value);
    messageList.push(newMessage);
    addMessage(newMessage);
    store(messageList);
    text.value = '';

}

function addMessage(message) {
    if (!message)
    return;
    var item = createItem(message);
    var items = document.getElementsByClassName('b-field')[0];
    items.appendChild(item);
}

function createItem(message) {
    var divItem = document.createElement('div');
    divItem.id = message.id;
    var textMessage = document.createElement('p');
    divItem.classList.add('messageStyle');
    textMessage.setAttribute('type', 'value');
    var checkbox = document.createElement('input');
    checkbox.setAttribute('type', 'checkbox');
    divItem.appendChild(textMessage);
    divItem.appendChild(checkbox);
    divItem.appendChild(document.createTextNode(message.name + ' :   ' + message.message));

    return divItem;
}

var theMessage = function(text){
    return {
        name:user,
        message:text,
        check:true,
        id :uniqueId()
    };
};

function deleteMsg() {
    var messages = document.getElementsByClassName('Check');
    if (messages.length != 0) {
        for (var i = messages.length - 1; i >= 0; i--) {
            var id = messages[i].id;
            for (var j = 0; j < messageList.length; j++) {
                if (id == messageList[j].id) {
                    messageList[j].message = 'deleted';
                }
            }
            var message = messages[i].innerHTML;
            var pos = message.lastIndexOf(':') + 4;
            var oldMessage = message.substring(pos);
            message = message.replace(oldMessage, 'deleted');
            messages[i].innerHTML = message;
            setMarker(messages[i]);
        }
    }
    store(messageList);
}

function editMsg() {
    var messages = document.getElementsByClassName('Check');
    if (messages.length > 1) {
        alert("You chose too much messages! Please choose one");
        return;
    }
    var messageIn =  messages[0].innerHTML;
    var pos = messageIn.lastIndexOf(':') + 4;
    var messageOut = messageIn.substring(pos);
    var messageNew = prompt("Edit message:", messageOut);
    var id = messages[0].id;
    for (var i = 0; i < messageList.length; i++) {
        if (id == messageList[i].id) {
            messageList[i].message = messageNew;
        }
    }
    messageIn = messageIn.replace(messageOut, messageNew);
    messages[0].innerHTML = messageIn;
    setMarker(messages[0]);
    store(messageList);
}

function store(messageList) {
    if(typeof(Storage) == "undefined") {
        alert('localStorage is not accessible');
        return;
    }

    var listAsString = JSON.stringify(messageList);
    localStorage.setItem("Messages list", listAsString);
}

function restore() {
    if(typeof(Storage) == "undefined") {
        alert('localStorage is not accessible');
        return;
    }

    var item = localStorage.getItem("Messages list");
    return item && JSON.parse(item);
}

function createAllMessages(allMessages) {
    for (var i = 0; i < allMessages.length; i++) {
        messageList.push(allMessages[i]);
        addMessage(allMessages[i]);
    }
    if (allMessages.length > 1)
    user = allMessages[allMessages.length - 1].name;
}
