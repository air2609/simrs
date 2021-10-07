var lastRow='';
var index=0;
var type = "";
var appContext = "/ghost/";
var framelastRow='';
var isModalExist = false;
var ghost_js = true;
var focussed;
var submitButton;
var arrObj= new Array();
var originalValues = new Array();
var hiddenOrgValues;
var objIndex=0;
var isRightClickDisabled = false;
var OrderWin = new Array();
var ghostMainWindow;
var closeInNewModeIsCancelled=false;
var anyError=false;
var childs = new Array();
var isPrint = false;
var counter = 0;

var navigationArray_dis = new Array(new Image(),new Image(), new Image(), new Image());
var navigationArray_ena = new Array(new Image(),new Image(), new Image(), new Image());

navigationArray_dis[0].src = appContext+"image/moveLast_dis.gif";
navigationArray_dis[1].src = appContext+"image/moveFirst_dis.gif";
navigationArray_dis[2].src = appContext+"image/movePrev_dis.gif";
navigationArray_dis[3].src = appContext+"image/moveNext_dis.gif";

navigationArray_ena[0].src = appContext+"image/moveLast.gif";
navigationArray_ena[1].src = appContext+"image/moveFirst.gif";
navigationArray_ena[2].src = appContext+"image/movePrev.gif";
navigationArray_ena[3].src = appContext+"image/moveNext.gif";

//This will call the script to autoclose all window when logout or Session Expired
closeAllWindowOnLogout();
window.onbeforeunload = releaseLock;
window.attachEvent("onload",addToolTips);
window.attachEvent('onload',decodeHiddenToOriginalValues);
window.attachEvent('onload',attachPrintButtonEvent);
function frameselected(framecurrRow){
	if (framecurrRow != framelastRow){
		framelastRow.className='none';
      	framecurrRow.className='selected';
    	framelastRow=framecurrRow;
	}
}

function closeModal(obj) {
    if (obj.isModalExist) {
        V.window.alert(V.window.title+" has window modal currently opened. Pls Close this modal dialog?");
        setTimeout("closeModal("+obj+")",500);
    }
}


function enableAllButtons(doc) {
   if (doc==null) doc = document;
   var buttons = doc.body.getElementsByTagName("INPUT");
   for (var i=buttons.length-1; i>=0; i--) {
      if (buttons[i].type=="button" || buttons[i].type=="submit") {
         buttons[i].disabled = false;
      }
   }
}

function disableMainKeys()
{
    var i;
    if (window.event.keyCode==13){
    outfor:
        for (i=0;i<arrObj.length;i++) {
            if (focussed==null) break;
            if (arrObj[i].name==focussed.name){
                //to handle selection process in a combobox
                if (focussed.type=='select-one' && focussed.oldIndex==focussed.selectedIndex && focussed.numClick%2>0) {
                    return;
                }
                submitButton.click();
                break outfor;
            }
        }
    }

	//alert(event.keyCode);
        // disable refresh(F5), Search (F3), Full Screen(F11),F6, Escape,
	if (window.event.keyCode==116||window.event.keyCode==114||window.event.keyCode==122||window.event.keyCode==117||window.event.keyCode==27)
	{
		event.keyCode = 27;
		window.event.returnValue = false;
	}
	// disable all ALT key manipulation
	else if (window.event.altKey)
	{
		if (event.keyCode==36)
		{
			event.keyCode = 27;
			eval(false);
			window.cancelBubble = true;
			window.event.returnValue = false;
			return false;
		}
		window.event.returnValue = false;
	}
	else if (window.event.altLeft)
	{
		alert("letft alt");
		event.keyCode = 27;
		window.event.returnValue = false;
	}
	// disable all CTRL key manipulation
	else if (window.event.ctrlKey)
	{
	    if (event.keyCode==65|event.keyCode==67|event.keyCode==86|event.keyCode==88|event.keyCode==90){
	        //do nothing for ctrl+A,ctrl+C,ctrl+V,ctrl+X, mean that these keys are enabled
	    } else {
	        //if (event.srcElement.type!='file')
		      //  event.keyCode = 27;
		    window.event.returnValue = false;
		}
	}
	disableBackspace();
}


function disableBackspace() {
  var e, text;
  if (!e) e=window.event;
  obj=e.srcElement;
  if (e.keyCode==8){
    if (obj.type=='text'||obj.type=='textarea'||obj.type=='password'||obj.type=='file') {
      if(obj.disabled==false){
          return;
      }
    }
    e.keyCode=27;
    window.event.returnValue = false;
  }
}


function _isConfirm(sTextMessage, sButtons)
{
	var retval="";
	if (window.showModalDialog)
	{
		var aDialogArgument = new Array();
		aDialogArgument[0] = sTextMessage;
		aDialogArgument[1] = sButtons;

        retval = showModalDialog(appContext+'include/alertDialog.htm', aDialogArgument, "status:no;dialogWidth:420px;dialogHeight:175px;scroll:no;Help:no;");
	}
	else
	{
		alert("Internet Explorer 4.0 or later is required.")
	}

	return retval;
}

function isConfirm(sTextMessage, sButtons)
{
	var retval="";
	if (window.showModalDialog)
	{
		var aDialogArgument = new Array();
		aDialogArgument[0] = sTextMessage;
		aDialogArgument[1] = sButtons;
		aDialogArgument[2] = this;


		retval = showModalDialog(appContext+ 'include/alertDialog.htm', aDialogArgument, "status:no;dialogWidth:420px;dialogHeight:175px;scroll:no;Help:no;");

	}
	else
	{
		alert("Internet Explorer 4.0 or later is required.")
	}
    anyError = true;
	return retval;
}

function selected(currRow, className){
	if (currRow != null && currRow != lastRow){
	   if (className == null)
         lastRow.className='none';
      else
         lastRow.className=className;

        currRow.className='selected';

    	lastRow=currRow;
	}
}

function recomended(currRow){
	if (currRow != null){
      currRow.className='recommended';
	}
}

function gotfocus(currRow, cursorType){
    if (currRow.className != "selected") {
        if (cursorType == null) {
          cursorType = 'default';
        }
        if (currRow != lastRow){
            currRow.className='highlight';
        }
        currRow.style.cursor = cursorType;
    }
}

function lostfocus(currRow,OrgClass){
	if (currRow != lastRow && currRow.className != "selected"){
      	currRow.className=OrgClass;
	}
}

function open800600 (url, windowName) {

   if (childs != null) {
       for (i=0; i<childs.length; i++) {
          V = childs[i];
          if (V.name == windowName && !V.window.closed) {
             V.window.focus();
             return V.window;
          }
       }
    }
   return _open800600(url, windowName);
}

function _open800600(url, windowName) {
    var width = 790,height=544;
    var x = screen.width>800?(screen.width-width)/2:0;
    var y = screen.height>600?(screen.height-54-height)/2:0;
	win = window.open(url,windowName,'width='+width+',height='+height+',left='+x+',top='+y+',scrollbars=no,resizeable=no,toolbar=no,titlebar=no,status=no,max=no',false);
	V = new Object();
	V.name = windowName;
	V.window = win;
	childs[childs.length] = V;
    return win;
}

function OpenRetrieval(url, width, height, context, screenName){
        if (context == null) {
            context = appContext;
        }
    	x =  context+'include/retrievalFrame.jsp?url='+escape(url)+'&scrName='+(screenName==null?"":escape(screenName));
    	isModalExist = true;
        win = showModalDialog(x,window,"status:no;dialogWidth:680px;dialogHeight:490px;scroll:no;help:no");
        isModalExist = false;
        return win;
}

function OpenRetrievalCustom(url, width, height, context, screenName){
        if (context == null) {
            context = appContext;
        }
    	x =  context+'include/retrievalFrame.jsp?url='+escape(url)+'&scrName='+(screenName==null?"":escape(screenName));
    	isModalExist = true;
        win = showModalDialog(x,window,"status:no;dialogWidth:"+width+"px;dialogHeight:"+height+"px;scroll:no;help:no");
        isModalExist = false;
        return win;
}

function open600425(url){
    var width = 600, height=425;
    var x = (getbrowserwidth()-width)/2;
    var y = (getbrowserheight() - height)/2;
   window.open(url,'TMSD2_EX2','width='+width+',height='+height+',left='+x+',top='+y+',scrollbars=no,resizeable=no,toolbar=no,titlebar=no,status=no,max=no',false);
}


function doFirst(form) {
    with (form) {
        stFormAction.value="FIRST";
        submit();
    }
}

function doNext(form) {
    with (form) {
        stFormAction.value="NEXT";
        submit();
    }
}

function doPrev(form) {
    with (form) {
        stFormAction.value="PREV";
        submit();
    }
}

function doLast(form) {
    with (form) {
        stFormAction.value="LAST";
        submit();
    }
}


function getDate() {
   var today= new Date();
   var MM;
   var Month;

   MM=today.getMonth()+1;
   switch (MM){
      case 1:
         Month='January';
         break;
      case 2:
         Month='February';
             break;
      case 3:
         Month='Maret';
             break;
      case 4:
         Month='April';
             break;
      case 5:
         Month='May';
             break;
      case 6:
         Month='June';
             break;
      case 7:
         Month='July';
             break;
      case 8:
         Month='August';
             break;
      case 9:
         Month='September';
             break;
      case 10:
         Month='October';
             break;
      case 11:
         Month='November';
             break;
      case 12:
         Month='December';
             break;
   }

   document.write (today.getDate() + ' '+ (Month) + ' '+ today.getYear())
}

function goToMenu(menu) {
   F = document.menuForm
   F.stScreenId.value = menu;
   F.submit();
}

function goToSubMenu(control, url, windowName) {
   if (control != null && (control.className == "inactiveMenu" || control.className == "inactiveMenuMaster")) {
      return;
   }

   var i;

   if (childs != null) {
       for (i=0; i<childs.length; i++) {
          V = childs[i];
          if (V.name == windowName && !V.window.closed) {
             V.window.focus();
             return;
          }
       }
    }
   return _open800600(url, windowName);
}

function showLogin(){
   win = showModalDialog("Login.html",window,"status:no;dialogWidth:600px;dialogHeight:400px")
}

function disable(control) {
   control.className = "inactiveMenu";
}

function disableMaster(control) {
   control.className = "inactiveMenuMaster";
}

function enabled(control) {
   control.className = "ongotfocus";
}

function lostFocus(control) {
   control.className = "onlostfocus";
}

function setAddButton(form) {
   with(form){
      cmdAdd.disabled= false ;
      cmdSave.disabled = false ;
      cmdDelete.disabled = false ;
      cmdModify.disabled= true ;
      cmdAbort.disabled= false ;
      cmdClose.disabled = true;

      if (cmdOpenR!=null ) {
         cmdOpenR.disabled = true;
      }

      //disabled navigation button...
      cmdLast.style.background = "url('"+appContext+"image/moveLast_dis.gif') no-repeat center";
      cmdLast.disabled = true;
      cmdNext.style.background = "url('"+appContext+"image/moveNext_dis.gif') no-repeat center";
      cmdNext.disabled = true;
      cmdFirst.style.background = "url('"+appContext+"image/moveFirst_dis.gif') no-repeat center";
      cmdFirst.disabled = true;
      cmdPrev.style.background = "url('"+appContext+"image/movePrev_dis.gif') no-repeat center";
      cmdPrev.disabled = true;

      stFormAction.value="ADD";
   }
}

function setViewButton(form) {
   with(form){
      cmdAdd.disabled= false;
      cmdSave.disabled = true;
      cmdDelete.disabled = false;
      cmdModify.disabled= false;
      cmdAbort.disabled= true;
      cmdClose.disabled = false;

      if (form.cmdOpenR && cmdOpenR!=null ) {
         cmdOpenR.disabled = false;
      }

      //disabled navigation button...
      setNavButton(form);

      stFormAction.value="VIEW";
   }
}


function doAbort(form) {
   with(form) {
      if (form.intLastActiveRow && form.intActiveRow &&
          form.stFormAction && stFormAction.value=="ADD") {
          intActiveRow.value = intLastActiveRow.value;
      }
      stFormAction.value="ABORT";
      submit();
   }
}

function doClose(form) {
   with(form) {
      stFormAction.value="CLOSED";
      submit();
   }
   top.self.close();
   if(top.opener && !top.opener.closed){
        if (top.opener != null){
            top.opener.focus();
        }
    }

}

function doclick(i,form){
  index = i;
  with (form) {
      intActiveRow.value = parseInt(index) + 1;
      if (stFormAction.value == 'ADD') {
         // getting tr tag and its type whether new or old
         //var type = event.srcElement.type;
         if (type == 'old')
            cmdDelete.disabled = true;
         else
            cmdDelete.disabled = false;
      }
   }
}

function setButton(form) {
   if (form == null) {
      form = document.forms[0];
   }
    with (form) {
        if((intMaxPage.value == 0) || (intMaxPage.value == 1)){

            cmdLast.style.backgroundImage.src  = navigationArray_dis[0];
            cmdLast.disabled = true;
            cmdNext.style.backgroundImage.src = navigationArray_dis[3];
            cmdNext.disabled = true;
            cmdFirst.style.backgroundImage.src = navigationArray_dis[1];
            cmdFirst.disabled = true;
            cmdPrev.style.backgroundImage.src = navigationArray_dis[2];
            cmdPrev.disabled = true;

        } else if (intPage.value==intMaxPage.value) {
            //Last
            cmdLast.style.backgroundImage.src  = navigationArray_dis[0];
            cmdLast.disabled = true;
            cmdNext.style.backgroundImage.src = navigationArray_dis[3];
            cmdNext.disabled = true;
        } else if (intPage.value==1) {
             //first intPage
             cmdFirst.style.backgroundImage.src = navigationArray_dis[1];
             cmdFirst.disabled = true;
             cmdPrev.style.backgroundImage.src =  navigationArray_dis[2];
             cmdPrev.disabled = true;
        }
    }
}

/** Function to re-arrange navigation button **/
function setNavButton(form) {
    with (form) {
        if (intPage.value==0) intPage.value = 1;
        if (parseInt(intPage.value)<parseInt(intMaxPage.value)) {
            //enable Last
            cmdLast.style.background = "url('"+navigationArray_ena[0].src+"') no-repeat center";
            cmdLast.disabled = false;
            cmdNext.style.background = "url('"+navigationArray_ena[3].src+"') no-repeat center";
            cmdNext.disabled = false;
        }
        else {
            //disable Last
            cmdLast.style.background = "url('"+navigationArray_dis[0].src+"') no-repeat center";
            cmdLast.disabled = true;
            cmdNext.style.background = "url('"+navigationArray_dis[3].src+"') no-repeat center";
            cmdNext.disabled = true;
        }
        if (parseInt(intPage.value)>1) {
            //enable first page
            cmdFirst.style.background = "url('"+navigationArray_ena[1].src+"') no-repeat center";
            cmdFirst.disabled = false;
            cmdPrev.style.background = "url('"+navigationArray_ena[2].src+"') no-repeat center";
            cmdPrev.disabled = false;
        }
        else {
            //disable first page
            cmdFirst.style.background = "url('"+navigationArray_dis[1].src+"') no-repeat center";
            cmdFirst.disabled = true;
            cmdPrev.style.background = "url('"+navigationArray_dis[2].src+"') no-repeat center";
            cmdPrev.disabled = true;
        }
    }
}

function setModifyButton(form) {
    if (form.cmdOpenR!=null ) {
     form.cmdOpenR.disabled = true;
    }
    with(form){
      cmdAdd.disabled= true;
      cmdSave.disabled = false;
      cmdDelete.disabled = true;
      cmdModify.disabled = true;
      cmdAbort.disabled = false;
      cmdClose.disabled = true;
//      if (cmdOpen != null) {
 //       cmdOpen.disabled = true;
//      }


      //disabled navigation button...
      cmdLast.style.background = "url('"+appContext+"image/moveLast_dis.gif') no-repeat center";
      cmdLast.disabled = true;
      cmdNext.style.background = "url('"+appContext+"image/moveNext_dis.gif') no-repeat center";
      cmdNext.disabled = true;
      cmdFirst.style.background = "url('"+appContext+"image/moveFirst_dis.gif') no-repeat center";
      cmdFirst.disabled = true;
      cmdPrev.style.background = "url('"+appContext+"image/movePrev_dis.gif') no-repeat center";
      cmdPrev.disabled = true;
   }
}



function openWindow(url, width, height, newWindow)
{
  if (newWindow)
    window.open(url,'','channelmode=no, toolbar=no, location=no, directories=no, status=yes, menubar=no, scrollbars=no, resizable=no, width=' + width + ', height=' + height + ', top=' + (top.window.screen.height - height)/2 + ', left=' + (top.window.screen.width - width)/2);
//    top.window.open(url,'blank','channelmode=no, toolbar=no, location=no, directories=no, status=yes, menubar=yes, scrollbars=yes, resizable=no, width=' + width + ', height=' + height + ', top=' + (top.window.screen.height - height)/2 + ', left=' + (top.window.screen.width - width)/2);
  else
  {
    closeWindow();

    window.msgOpen = top.window.open(url,null,'channelmode=no, toolbar=no, location=no, directories=no, status=yes, menubar=no, scrollbars=no, resizable=no, width=' + width + ', height=' + height + ', top=' + (top.window.screen.height - height)/2 + ', left=' + (top.window.screen.width - width)/2);
//    top.window.msgOpen = top.window.open(url,null,'channelmode=no, toolbar=no, location=no, directories=no, status=yes, menubar=yes, scrollbars=yes, resizable=no, width=' + width + ', height=' + height + ', top=' + (top.window.screen.height - height)/2 + ', left=' + (top.window.screen.width - width)/2);
  }
}

function closeWindow()
{
  if (top.window.msgOpen != null && top.window.msgOpen.closed == false)
    top.window.msgOpen.close();
}

function callFrame1(url, status, windowName, _win) {
  var w = 800;
  var h = 540;

  if (window.screen.width == 800)
  {
    w = 790;
    h = 540;
  }

  if (windowName == null)
    return;

  return open800600(url, windowName);
}

function callFrame(url, status)
{
  var w = 800;
  var h = 540;

  if (window.screen.width == 800)
  {
    w = 790;
    h = 540;
  }

  if (status == null)
    status = true;

  return open800600(url, windowName);

  //window.open(url,'','channelmode=no, toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=no, resizable=no, width=' + w + ', height=' + h + ', top=' + (top.window.screen.height - h)/2 + ', left=' + (top.window.screen.width - w)/2);
 // openWindow(url, w, h, status);

//  openWindow(url, w, h, status);
}


function disableNavigationButtons(form)
{
  theForm = form;

  if (form == null) {
      theForm = document.forms[0];
   }

  with (theForm)
  {
	if(cmdLast != null){
        cmdLast.disabled = true;
        cmdNext.disabled = true;
        cmdFirst.disabled = true;
        cmdPrev.disabled = true;
    }
  }
}


function reg(a)
{
	var re = /'/ig;


	b = a.replace(re, "\\'");
	return b;
}

function clickIE() {
	if (document.all) {
		return false;
	}
}
function clickNS(e) {
	if (document.layers||(document.getElementById&&!document.all)) 	{
		if (e.which==2||e.which==3) {
			return false;
		}
	}
}
if (isRightClickDisabled){
    if (document.layers) {
	    document.captureEvents(Event.MOUSEDOWN);
	    document.onmousedown=clickNS;
    } else {
	    document.onmouseup=clickNS;
	    document.oncontextmenu=clickIE;
    }
    document.oncontextmenu=new Function("if (window.event.srcElement.type=='text'||window.event.srcElement.type=='textarea') "+
    "if (window.event.srcElement.disabled==false) return; return false");
}


function _OpenRetrieval(url, width, height, context, screenName){
       var _height = height;
       var _width = width;

       var width = 790,height=544;

       var x1 = screen.width>800?(screen.width-width)/2:0;
       var y1 = screen.height>600?(screen.height-51-height)/2:0;
       //alert(x1+','+y1)

       if (context == null) {
            context = appContext;
        }

    	if ( (_height==null) || (_height==0) )
    	{     _height="490px";  }

    	if ( (_width==null) || (_width==0) )
        {     _width="680px";   }

        _param = "status:no;dialogWidth:" + _width + ";dialogHeight:" + _height + ";dialogLeft:"+x1+"px;dialogTop:"+y1+"px;scroll:no;help:no";
      //  alert(_param);

    	x =  context+'include/retrievalFrame.jsp?url='+escape(url)+'&scrName='+(screenName==null?"":escape(screenName));
    	isModalExist = true;
        win = showModalDialog(x,window,_param);
        isModalExist = false;

        return win;
}



function EnableDisableAllFields(disable)
{
    _inputs = document.body.getElementsByTagName("INPUT");
    _len    = _inputs.length;

    for (i=0;i<_len;i++)
    {
        if (disable)
        {
            if ( (_inputs[i].disabled==false) && (_inputs[i].type=='text')  )
                _inputs[i].disabled = true;
        }
        else
        {
            if ( (_inputs[i].disabled==true) && (_inputs[i].type=='text')  )
                _inputs[i].disabled = false;
        }
    }
}


function setButtonInParent(form) {
   // the form is a the child/IFRAME form
   if (form == null) {
      return;
   }
    with (form)
    {
        if((intMaxPage.value == 0) || (intMaxPage.value == 1)){
            parent.document.all("cmdLast").style.background = "url('"+appContext+"image/moveLast_dis.gif') no-repeat center";
            parent.document.all("cmdLast").disabled = true;
            parent.document.all("cmdNext").style.background = "url('"+appContext+"image/moveNext_dis.gif') no-repeat center";
            parent.document.all("cmdNext").disabled = true;
            parent.document.all("cmdFirst").style.background = "url('"+appContext+"image/moveFirst_dis.gif') no-repeat center";
            parent.document.all("cmdFirst").disabled = true;
            parent.document.all("cmdPrev").style.background = "url('"+appContext+"image/movePrev_dis.gif') no-repeat center";
            parent.document.all("cmdPrev").disabled = true;

        } else if (intPage.value==intMaxPage.value) {
            //Last
            parent.document.all("cmdFirst").style.background = "url('"+appContext+"image/moveFirst.gif') no-repeat center";
            parent.document.all("cmdFirst").disabled = false;
            parent.document.all("cmdPrev").style.background = "url('"+appContext+"image/movePrev.gif') no-repeat center";
            parent.document.all("cmdPrev").disabled = false;

            parent.document.all("cmdLast").style.background = "url('"+appContext+"image/moveLast_dis.gif') no-repeat center";
            parent.document.all("cmdLast").disabled = true;
            parent.document.all("cmdNext").style.background = "url('"+appContext+"image/moveNext_dis.gif') no-repeat center";
            parent.document.all("cmdNext").disabled = true;
        } else if (intPage.value==1) {
             //first intPage
            parent.document.all("cmdFirst").style.background = "url('"+appContext+"image/moveFirst_dis.gif') no-repeat center";
            parent.document.all("cmdFirst").disabled = true;
            parent.document.all("cmdPrev").style.background = "url('"+appContext+"image/movePrev_dis.gif') no-repeat center";
            parent.document.all("cmdPrev").disabled = true;

            parent.document.all("cmdLast").style.background = "url('"+appContext+"image/moveLast.gif') no-repeat center";
            parent.document.all("cmdLast").disabled = false;
            parent.document.all("cmdNext").style.background = "url('"+appContext+"image/moveNext.gif') no-repeat center";
            parent.document.all("cmdNext").disabled = false;

        }

        if (stFormAction.value=="VIEW")
        {
            parent.document.forms[0].cmdSave.disabled = true;
            parent.document.forms[0].cmdAdd.disabled = false;
            parent.document.forms[0].cmdDelete.disabled = false;
            parent.document.forms[0].cmdModify.disabled = false;
            parent.document.forms[0].cmdAbort.disabled = true;
        }

        if (stFormAction.value=="MODIFY")
        {
            parent.document.forms[0].cmdSave.disabled = false;
            parent.document.forms[0].cmdAdd.disabled = true;
            parent.document.forms[0].cmdDelete.disabled = true;
            parent.document.forms[0].cmdModify.disabled = true;
            parent.document.forms[0].cmdAbort.disabled = false;
            parent.document.forms[0].cmdClose.disabled = true;
        }
    }
}

function disableNavigationButtonsIFrames(form)
{
  theForm = form;

  // parameter form will be the parent form
  if (form == null) {
      theForm = document.forms[0];
   }

  with (theForm)
  {
	cmdLast.disabled = true;
	cmdLast.style.background = "url('"+appContext+"image/moveLast_dis.gif') no-repeat center";

	cmdNext.disabled = true;
	cmdNext.style.background = "url('"+appContext+"image/moveNext_dis.gif') no-repeat center";

	cmdFirst.disabled = true;
    cmdFirst.style.background = "url('"+appContext+"image/moveFirst_dis.gif') no-repeat center";

	cmdPrev.disabled = true;
	cmdPrev.style.background = "url('"+appContext+"image/movePrev_dis.gif') no-repeat center";
  }
}


function isSelected()
{
    // this function is to check whether the user has select any record
   var e = getSelected();

   if (e[0]!=null)
        d = parseInt(e[0].rowIndex);
   else
        d = parseInt(-1);

   return d;
}

function getSelectedRecord()
{
    return lastSelected;
}

function initNavigationButton(form,iframes)
{
    with (form)
    {
      if (iframes)
      {
        _parent = parent.document.forms[0];
        if(intMaxPage.value == 1)
        {
            // only 1 page
            _parent.cmdLast.style.background = "url('"+appContext+"image/moveLast_dis.gif') no-repeat center";
            _parent.cmdLast.disabled = true;
            _parent.cmdNext.style.background = "url('"+appContext+"image/moveNext_dis.gif') no-repeat center";
            _parent.cmdNext.disabled = true;
            _parent.cmdFirst.style.background = "url('"+appContext+"image/moveFirst_dis.gif') no-repeat center";
            _parent.cmdFirst.disabled = true;
            _parent.cmdPrev.style.background = "url('"+appContext+"image/movePrev_dis.gif') no-repeat center";
            _parent.cmdPrev.disabled = true;

        } else if (intPage.value==intMaxPage.value) {
            //Last
            _parent.cmdFirst.style.background = "url('"+appContext+"image/moveFirst.gif') no-repeat center";
            _parent.cmdFirst.disabled = false;
            _parent.cmdPrev.style.background = "url('"+appContext+"image/movePrev.gif') no-repeat center";
            _parent.cmdPrev.disabled = false;

            _parent.cmdLast.style.background = "url('"+appContext+"image/moveLast_dis.gif') no-repeat center";
            _parent.cmdLast.disabled = true;
            _parent.cmdNext.style.background = "url('"+appContext+"image/moveNext_dis.gif') no-repeat center";
            _parent.cmdNext.disabled = true;
        } else if ( (intPage.value==1) && (intMaxPage.value>1) ) {
             //first intPage
            _parent.cmdLast.style.background = "url('"+appContext+"image/moveLast.gif') no-repeat center";
            _parent.cmdLast.disabled = false;
            _parent.cmdNext.style.background = "url('"+appContext+"image/moveNext.gif') no-repeat center";
            _parent.cmdNext.disabled = false;

            _parent.cmdFirst.style.background = "url('"+appContext+"image/moveFirst_dis.gif') no-repeat center";
            _parent.cmdFirst.disabled = true;
            _parent.cmdPrev.style.background = "url('"+appContext+"image/movePrev_dis.gif') no-repeat center";
            _parent.cmdPrev.disabled = true;
        } else if ( (intPage.value>1) && (intMaxPage.value>1) )
        {
            _parent.cmdLast.style.background = "url('"+appContext+"image/moveLast.gif') no-repeat center";
            _parent.cmdLast.disabled = false;
            _parent.cmdNext.style.background = "url('"+appContext+"image/moveNext.gif') no-repeat center";
            _parent.cmdNext.disabled = false;

            _parent.cmdFirst.style.background = "url('"+appContext+"image/moveFirst.gif') no-repeat center";
            _parent.cmdFirst.disabled = false;
            _parent.cmdPrev.style.background = "url('"+appContext+"image/movePrev.gif') no-repeat center";
            _parent.cmdPrev.disabled = false;
        }
      }
      else
      {
        if(intMaxPage.value == 1)
        {
            // only 1 page
            cmdLast.style.background = "url('"+appContext+"image/moveLast_dis.gif') no-repeat center";
            cmdLast.disabled = true;
            cmdNext.style.background = "url('"+appContext+"image/moveNext_dis.gif') no-repeat center";
            cmdNext.disabled = true;
            cmdFirst.style.background = "url('"+appContext+"image/moveFirst_dis.gif') no-repeat center";
            cmdFirst.disabled = true;
            cmdPrev.style.background = "url('"+appContext+"image/movePrev_dis.gif') no-repeat center";
            cmdPrev.disabled = true;

        } else if (intPage.value==intMaxPage.value) {
            //Last
            cmdFirst.style.background = "url('"+appContext+"image/moveFirst.gif') no-repeat center";
            cmdFirst.disabled = false;
            cmdPrev.style.background = "url('"+appContext+"image/movePrev.gif') no-repeat center";
            cmdPrev.disabled = false;

            cmdLast.style.background = "url('"+appContext+"image/moveLast_dis.gif') no-repeat center";
            cmdLast.disabled = true;
            cmdNext.style.background = "url('"+appContext+"image/moveNext_dis.gif') no-repeat center";
            cmdNext.disabled = true;
        } else if ( (intPage.value==1) && (intMaxPage.value>1) ) {
             //first intPage
            cmdLast.style.background = "url('"+appContext+"image/moveLast.gif') no-repeat center";
            cmdLast.disabled = false;
            cmdNext.style.background = "url('"+appContext+"image/moveNext.gif') no-repeat center";
            cmdNext.disabled = false;

             cmdFirst.style.background = "url('"+appContext+"image/moveFirst_dis.gif') no-repeat center";
             cmdFirst.disabled = true;
             cmdPrev.style.background = "url('"+appContext+"image/movePrev_dis.gif') no-repeat center";
             cmdPrev.disabled = true;
        } else if ( (intPage.value>1) && (intMaxPage.value>1) )
        {
            cmdLast.style.background = "url('"+appContext+"image/moveLast.gif') no-repeat center";
            cmdLast.disabled = false;
            cmdNext.style.background = "url('"+appContext+"image/moveNext.gif') no-repeat center";
            cmdNext.disabled = false;

            cmdFirst.style.background = "url('"+appContext+"image/moveFirst.gif') no-repeat center";
            cmdFirst.disabled = false;
            cmdPrev.style.background = "url('"+appContext+"image/movePrev.gif') no-repeat center";
            cmdPrev.disabled = false;
        }
      }
    }
}

function getbrowserwidth(){
    if (navigator.userAgent.indexOf("MSIE") > 0) {
        return(document.body.clientWidth);
        //return top.window.screen.width;
    } else {
        return window.outerWidth;
    }
}

function getbrowserheight(){
    if (navigator.userAgent.indexOf("MSIE") > 0){
        //return top.window.screen.height;
        return(document.body.clientHeight);
    } else {
        return(window.outerHeight);
    }
}

function releaseLock() {
    if (isPrint) {
        if (counter == 0) {
            counter++;
        } else if (counter == 1) {
            isPrint = false;
        }
        return;
    }
    var inputElements;
    var saveButton;
    var formActionField;
    var abortButton;
    var addButton;
    if (document.forms.length>0) {
        inputElements= (document.forms[0].tags("INPUT"));
        // Loop through all form's element to search the 'Save' and 'Abort 'button
        for (i=0;i<inputElements.length;i++){
            if (inputElements[i].type =='button' || inputElements[i].type =='submit') {
                if (inputElements[i].value.toUpperCase()=='SAVE') {
                    saveButton = inputElements[i];
                } else if (inputElements[i].value.toUpperCase()=='ABORT') {
                    abortButton = inputElements[i];
                } else if (inputElements[i].value.toUpperCase()=='ADD') {
                    addButton = inputElements[i];
                }
                inputElements[i].oldDisabled = inputElements[i].disabled;
                inputElements[i].disabled = true;
            }
        } // end for

        if (document.forms[0].stModAction!=null)
            formActionField = document.forms[0].stModAction;

        if (document.forms[0].stFormAction!=null)
            formActionField = document.forms[0].stFormAction;

        if ((formActionField!=null)&&(saveButton !=null)&&(abortButton!=null)){
            if ((formActionField.value.toUpperCase()=='MODIFY')&&(saveButton.oldDisabled==false)){
                alert('Undefined error - Contact Administrator!');
                abortButton.disabled = false;
                abortButton.click();
            }
        }
        if ((formActionField!=null)&&(saveButton !=null)&&(addButton!=null)){
            if (((formActionField.value.toUpperCase()=='ADD')||(formActionField.value.toUpperCase()=='NEW'))&&(abortButton.oldDisabled==false)){
                //in case screen in add mode, if the user press cancel in the dialog mode
                closeInNewModeIsCancelled = true;
                restoreAllButtons();
                return 'Undefined error - Contact Administrator!';
                abortButton.disabled = false;
                abortButton.click();
            }
        }
   } // end if document.forms.length>0
}

function attachPrintButtonEvent() {
    if (document.forms.length<=0) return;
    var inputs= document.forms[0].tags("INPUT");
    for (var i=0; i<inputs.length; i++) {
        if (inputs[i].type=="button" && " Print Download Generate ASCII ".indexOf(inputs[i].value.trim())>0) {
            var oldFunction = inputs[i].onclick;
            inputs[i].onclick = printClicked;
            inputs[i].VH_click = oldFunction;
        }
    }
}

function printClicked() {
      isPrint = true;
      counter = 0;
      if (this.VH_click != null) {
        this.VH_click();
      }
}

function restoreAllButtons() {
    var inputElements= (document.forms[0].tags("INPUT"));
    // Loop through all form's element to search the 'Save' and 'Abort 'button
    for (var i=0; i<inputElements.length; i++){
        if (inputElements[i].type == "button") {
            inputElements[i].disabled = inputElements[i].oldDisabled;
        }
    } // end for
}

function submitOnEnter(obj,button){
        //alert(obj);
        //alert('registering '+obj.name);
        obj.attachEvent("onfocus",getFocussed);
        obj.attachEvent("onblur",getUnfocussed)
        if (obj.type=='select-one') {
            obj.attachEvent("onclick",selectClicked);
        }
        arrObj[objIndex]=obj;
        objIndex++;
        submitButton=button;
}
function selectClicked() {
    if (event.srcElement.numClick==null)
        event.srcElement.numClick=1;
    else
        event.srcElement.numClick++;
}
function registerSubmitFields(objArr,button){
    var i;
    arrObj =objArr;
    for (i=0;i<objArr.length;i++){
        objArr[i].attachEvent("onfocus",getFocussed);
        objArr[i].attachEvent("onblur",getUnfocussed)
        if (objArr[i].type=='select-one') {
            objArr[i].attachEvent("onclick",selectClicked);
        }

    }
    submitButton=button;
}
function getFocussed(){
    focussed = event.srcElement;
    if (focussed.type=='select-one') {
        focussed.oldIndex=focussed.selectedIndex;
    }

}
function getUnfocussed(){
    focussed = document.body;
}
function closeAllWindowOnLogout(){
      var j,op,currw;
      ghostMainWindow==null;
      currw=window;
      if (window.top.opener==null)
        return;
      if (window.top.opener.closed)
        return;
      op = window.top.opener;
      j=0;
      outer:
      while (currw!=null) {
        //alert('j='+j+', top.name='+currw.top.name);
        if (currw.top.opener==null) {
            //alert('opener closed, check logout will be disabled');
            return;
        }
        op=currw.top.opener;
        if (currw.top.name=='GHOSTMAINWINDOW') {
            //alert(currw.top.name);
            ghostMainWindow= currw.top;
            break outer;
        }
        j++;
        currw = op;
        if (j>12) break outer;
      }
    if (ghostMainWindow!=null) {
        //alert('tmsd2 main window found. setting auto close...')
        ghostMainWindow.resetTimeout();
        ghostMainWindow.inLoginScreen=false;
        checkLoginStatus();
    }
}

//to check login status, if login==false because session time out than close window
function checkLoginStatus() {
    if (ghostMainWindow.closed) {
        window.top.close();
        return;
    }
    if(!ghostMainWindow.login) {
        if (window.top.name!='ghostMainWindow') {
            window.top.close();
        }
    }
// this code is deprecated on 11-09-2003, when parent is closed, child window don't need to be closed.
//    if (window.top.opener.closed) {
//        if (window.top.name!='ghostMainWindow') {
//            if (!closeInNewModeIsCancelled) window.top.close();
//       }
//    }
    setTimeout("checkLoginStatus()",500);
}

function addToolTips() {
    var inputs = document.getElementsByTagName("INPUT");
    for (var i=inputs.length; --i>=0; ) {
        if (inputs[i].type == "text") {
            inputs[i].title = inputs[i].value;
            if (inputs[i].onblur != null) {
                inputs[i].attachEvent('onblur',objectWithBlur);
            } else {
                inputs[i].onblur = function() {this.title = this.value;};
            }
        }
    }
}

function objectWithBlur() {
    if (event.srcElement.type == 'text')
        event.srcElement.title = event.srcElement.value;
}

// Function to disable all buttons
function disableAllButtons(doc) {
   if (doc==null) doc = document;
   var buttons = doc.body.getElementsByTagName("INPUT");
   for (var i=buttons.length-1; i>=0; i--) {
      if (buttons[i].type=="button" || buttons[i].type=="submit") {
         buttons[i].disabled = true;
      }
   }
}

function backupAllData(){
    var inputs = document.body.getElementsByTagName("INPUT");
    var dropdowns = document.body.getElementsByTagName("SELECT");
    var index=0;
    for (var i=0;i<inputs.length;i++) {
        if (inputs[i].type=="text" | inputs[i].type=="textarea") {
            originalValues[index] = inputs[i].name+"||"+inputs[i].value;
            index++;
        }  else if (inputs[i].type=="radio" || inputs[i].type=="checkbox") {
            originalValues[index] = inputs[i].name+"||"+inputs[i].checked;
            index++;
        }  else if (inputs[i].type=="button") {
            originalValues[index] = inputs[i].name+"||"+inputs[i].disabled;
            index++;
        }
    }
    for (var i=0;i<dropdowns.length;i++){
        if (dropdowns[i].type=="select-one") {
            originalValues[index] = dropdowns[i].name+"||"+dropdowns[i].selectedIndex;
            index++;
        }
    }
    putOrgValuesInHiddenField();
}

function restoreAllData(){
    var navButtonCount=1;
    var pair;
    var inputs = document.body.getElementsByTagName("INPUT");
    var dropdowns = document.body.getElementsByTagName("SELECT");
    var index=0;
    for (var i=0;i<inputs.length;i++) {
        if (inputs[i].type=="text" | inputs[i].type=="textarea") {
            pair=originalValues[index];
            inputs[i].value = pair.substring(pair.indexOf('||')+2);
            index++;
        } else if (inputs[i].type=="radio" | inputs[i].type=="checkbox") {
            pair=originalValues[index];
            if (pair.substring(pair.indexOf('||')+2)=='true')
                inputs[i].checked = true;
            else
                inputs[i].checked = false;
            index++;
        } else if (inputs[i].type=="button") {
            pair=originalValues[index];
            if (pair.substring(pair.indexOf('||')+2)=='true')
                inputs[i].disabled= true;
            else
                inputs[i].disabled= false;
            // set image for navigation button, note that navButtonCount must be arrange according to the
            // navigationArray_dis and navigationArray_ena array
            if ((inputs[i].style.backgroundImage).length>0){
                if (inputs[i].disabled==true) {
                    inputs[i].style.backgroundImage.src = navigationArray_dis[navButtonCount>=4?navButtonCount-4:navButtonCount];
                    inputs[i].style.background= "url('"+navigationArray_dis[navButtonCount>=4?navButtonCount-4:navButtonCount].src+"') no-repeat center";
                }
                else if (inputs[i].disabled==false) {
                    inputs[i].style.backgroundImage.src = navigationArray_ena[navButtonCount>=4?navButtonCount-4:navButtonCount];
                    inputs[i].style.background= "url('"+navigationArray_ena[navButtonCount>=4?navButtonCount-4:navButtonCount].src+"') no-repeat center";
                }
                navButtonCount++;
            }

            index++;
        }
    }

    for (var j=0;j<dropdowns.length;j++){
        if (dropdowns[j].type=="select-one") {
            pair=originalValues[index];
            dropdowns[j].selectedIndex = pair.substring(pair.indexOf('||')+2);
            index++;
        }
    }

}

function putOrgValuesInHiddenField(){
    var obHidden=document.createElement("INPUT");
    if (document.forms[0].hiddenOrgValues !=null) {
        document.forms[0].hiddenOrgValues.value=encodeToHiddenValue();
        hiddenOrgValues=obHidden.value;
        return;
    }

    obHidden.type='hidden';
    obHidden.name='hiddenOrgValues';
    obHidden.id='hiddenOrgValues';

    document.forms[0].appendChild(obHidden);

    obHidden.value=encodeToHiddenValue();
    hiddenOrgValues=obHidden.value;
}
function encodeToHiddenValue(){
    var encodedValue='';
    for (var i=0;i<originalValues.length;i++) {
        encodedValue=encodedValue+originalValues[i]+';;';
    }
    return encodedValue;
}

function decodeHiddenToOriginalValues(){
    if (hiddenOrgValues!=null ) {
        originalValues=hiddenOrgValues.split(';;');
        if (anyError)putOrgValuesInHiddenField();
    }
}

String.prototype.trim=function (){
   return this.replace(/^\s+/,"").replace(/\s+$/,"");
}

//======================
//Common Screen Function
//======================

//rows is common name for collection of row.
//don't use this function if you specify different name for collection of row
function changeID(form) {
	with(document.form){
        for (var i=0; i<document.all.form.rows.length; i++) {
            document.all.form.rows[i].id = (i+1);
        }
	}
}

function setErrorButton(form){
    with(form){
      cmdClose.disabled= true;
      cmdAdd.disabled= true;
      cmdAddType.disabled = true;
      cmdSave.disabled = false;
      cmdDelete.disabled = false;
      cmdModify.disabled = true;
      cmdAbort.disabled= false;
      disabledNavButton();
    }
}

function setReadOnlyButton(form){
    with(form){
      cmdClose.disabled= false;
      cmdAdd.disabled= true;
      cmdSave.disabled = true;
      cmdDelete.disabled = true;
      cmdModify.disabled = true;
      cmdAbort.disabled= true;
    }
}

function disabledNavButton(form) {
    with (form) {
         cmdLast.style.background = "url('../../../../image/moveLast_dis.gif') no-repeat center";
         cmdLast.disabled = true;
         cmdNext.style.background = "url('../../../../image/moveNext_dis.gif') no-repeat center";
         cmdNext.disabled = true;
         cmdFirst.style.background = "url('../../../../image/moveFirst_dis.gif') no-repeat center";
         cmdFirst.disabled = true;
         cmdPrev.style.background = "url('../../../../image/movePrev_dis.gif') no-repeat center";
         cmdPrev.disabled = true;
    }
}