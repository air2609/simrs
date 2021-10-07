/*
var lastSelected = new Array (null, null);
var pk = "";
var KEYDOWN = 40;
var KEYUP = 38;
var PAGEDOWN = 34;
var PAGEUP = 33;
			
function changeColor(obj, hdn, val)
{
  if (lastSelected[0] != null)
    lastSelected[0].style.backgroundColor = lastSelected[1];

  lastSelected[0] = obj;
  lastSelected[1] = obj.style.backgroundColor;
					
  obj.style.backgroundColor = "green";
  
  if (typeof(hdn) != "object")
    hdn.value = val;
  else
  {
    for (var x = 0; x < hdn.length; x++)
      hdn[x].value = val[x];
  }
}
		
function keyDOWN(idName, idx)
{
//  parent.document.all.iresultd.scrolling = "no";

  if (document.all(idName).length != null)
  {
    switch (event.keyCode)
    {
      case KEYDOWN:
      {
     	idx++;

	if (idx < document.all(idName).length)
	{
	  document.all(idName, idx).focus();
	  document.all(idName, idx).onclick();
//	  document.all(idName, idx).scrollIntoView(true);
	}
	break;
      }
						
      case KEYUP:
      {
   	idx--;
							
	if (idx >= 0)
	{
	  document.all(idName, idx).focus();
	  document.all(idName, idx).onclick();
//	  document.all(idName, idx).scrollIntoView(true);
	}
	break;
      }
    }
  }

//  parent.document.all.iresultd.scrolling = "auto";
}
*/


// new Array (object, background color, name of primary key (hidden), form name)
var lastSelected = new Array (null, null, null, null);

// new Array (object, background color)
var mouseOver = new Array(null, null);

// 0 = Initial, 1 = New Entry, 2 = Add, 3 = Modify
var curMode = 0;
	
function clickSelected(obj, val, name, frm)
{
	var status = false;
	var mode = parseInt(curMode);
	
	if (mode == 0 || mode == 1)
		status = true;
	else if (mode == 2 && val[0] == "")
		status = true;
	
	if (status)
	{
		if (obj != lastSelected[0])
		{
			resetSelected();

			if (obj != null)
			{
				
				lastSelected[0] = obj;
				
				if (obj != mouseOver[0])
					lastSelected[1] = obj.className;
				else
					lastSelected[1] = mouseOver[1];
				
				lastSelected[2] = name;
				lastSelected[3] = frm;
				
				//if (name!=null)
				//{
				//for (var x = 0; x < name.length; x++)
				//	document.forms(frm).elements(name[x]).value = new String(val[x]);
				//}
				
				changeColor(obj, 2);
			}
		}
	}
}

function resetSelected()
{
	
	if (lastSelected[0] != null)
	{
		lastSelected[0].className = lastSelected[1];

		//for (var x = 0; x < lastSelected[2].length; x++)
		//	document.forms(lastSelected[3]).elements(lastSelected[2][x]).value = "";
	}

	lastSelected[0] = null;
	lastSelected[1] = null;
	lastSelected[2] = null;
	lastSelected[3] = null;
}

function changeColor(obj, status)
{
	switch (status)
	{
		case 0: 
		{
			if (obj == mouseOver[0])
			{
				if (obj == lastSelected[0])
					obj.className = "selected";
				else
					obj.className = mouseOver[1];
				
				mouseOver[0] = null;
				mouseOver[1] = null;
			}
			
			break;
		}
							
		case 1: 
		{
			if (mouseOver[0] != null)
			{
				if (mouseOver[0] == lastSelected[0])
					mouseOver[0].className = "selected";
				else
					mouseOver[0].className = mouseOver[1];
			}
			
			mouseOver[0] = obj;
			
			if (obj == lastSelected[0])
				mouseOver[1] = lastSelected[1];
			else
			{
				mouseOver[1] = obj.className;						
				obj.className = "highlight";
			}
			break;
		}
												
		case 2: obj.className = "selected";
				break;
	}
}

function changeTab(idTab, idx)
{
	var tabNo;
				
	if (document.all(idTab).length > 1)
	{
		for (var x = 0; x < document.all(idTab).length; x++)
		{
			tabNo = x + 1;
						
			if (idx == tabNo)
			{
				document.all(idTab, x).className = "tabselected";
				document.all(idTab + tabNo).style.display = 'inline';
			}
			else
			{
				document.all(idTab, x).className = "tabnormal";
				document.all(idTab + tabNo).style.display = 'none';
			}
		}
	}
}

function openWindow(url, width, height, newWindow)
{
  if (newWindow)
    return window.open(url,'','channelmode=no, toolbar=no, location=no, directories=no, status=yes, menubar=no, scrollbars=no, resizable=no, width=' + width + ', height=' + height + ', top=' + (top.window.screen.height - height)/2 + ', left=' + (top.window.screen.width - width)/2);
//    top.window.open(url,'blank','channelmode=no, toolbar=no, location=no, directories=no, status=yes, menubar=yes, scrollbars=yes, resizable=no, width=' + width + ', height=' + height + ', top=' + (top.window.screen.height - height)/2 + ', left=' + (top.window.screen.width - width)/2);
  else
  {
    closeWindow();

    return window.msgOpen = top.window.open(url,null,'channelmode=no, toolbar=no, location=no, directories=no, status=yes, menubar=no, scrollbars=no, resizable=no, width=' + width + ', height=' + height + ', top=' + (top.window.screen.height - height)/2 + ', left=' + (top.window.screen.width - width)/2);
//    top.window.msgOpen = top.window.open(url,null,'channelmode=no, toolbar=no, location=no, directories=no, status=yes, menubar=yes, scrollbars=yes, resizable=no, width=' + width + ', height=' + height + ', top=' + (top.window.screen.height - height)/2 + ', left=' + (top.window.screen.width - width)/2);
  }
}

function closeWindow()
{
  if (top.window.msgOpen != null && top.window.msgOpen.closed == false)
    top.window.msgOpen.close();
}

function callFrame(url, status)
{
  var w = 800;
  var h = 600;

  if (window.screen.width == 800)
  {
    w = 790;
    h = 500;
  }

  if (status == null)
    status = true;

  return openWindow(url, w, h, status);
}

function submitForm(frm)
{
	document.forms(frm).submit();
}

function changeScreen(url)
{
	document.location = url;
}

function changeCheck(obj, chg)
{
  if (obj.checked == true)
  {
    if (chg.length > 0)
    {
      for (var x = 0; x < chg.length; x++)
      {
        chg(x).checked = true;
      }
    }
    else
      chg.checked = true;
  }
  else
  {
    if (chg.length > 0)
    {
      for (var x = 0; x < chg.length; x++)
      {
        chg(x).checked = false;
      }
    }
    else
      chg.checked = false;
  }
}

function enableAll(obj, iddiv, selected)
{
	var type;
	var tsc;
	var status = false;
		
	if (selected)
		tsc = document.all(iddiv).all.tags('INPUT');
	else if (lastSelected[0] != null)
		tsc = lastSelected[0].all.tags('INPUT');
	else
		tsc = null;

	if (tsc != null)
	{
		for (var x = 0; x < tsc.length; x++)
		{
			type = tsc[x].type;
						
			if (type == 'text' || type == 'checkbox' || type == 'radio' || type == 'password' || type == 'file')
			{
				if (disOrEna(tsc[x], false))
					status = true;
			}
		}
	}
	
	if (selected)
		tsc = document.all(iddiv).all.tags('SELECT');
	else if (lastSelected[0] != null)
		tsc = lastSelected[0].all.tags('SELECT');
	else
		tsc = null;

	if (tsc != null)
	{
		for (var x = 0; x < tsc.length; x++)
		{
			if (disOrEna(tsc[x], false))
				status = true;
		}
	}
	
	if (selected)
		tsc = document.all(iddiv).all.tags('TEXTAREA');
	else if (lastSelected[0] != null)
		tsc = lastSelected[0].all.tags('TEXTAREA');
	else
		tsc = null;
	
	if (tsc != null)
	{
		for (var x = 0; x < tsc.length; x++)
		{
			if (disOrEna(tsc[x], false))
				status = true;
		}
	}
	
	if (status)
		disOrEnaButton(obj.name);
}

function disableAll(obj, iddiv, selected)
{
	var type;
	var tsc;
	var status = false;
		
	if (selected)
		tsc = document.all(iddiv).all.tags('INPUT');
	else if (lastSelected[0] != null)
		tsc = lastSelected[0].all.tags('INPUT');
	else
		tsc = null;

	if (tsc != null)
	{
		for (var x = 0; x < tsc.length; x++)
		{
			type = tsc[x].type;
						
			if (type == 'text' || type == 'checkbox' || type == 'radio' || type == 'password' || type == 'file')
			{
				if (disOrEna(tsc[x], true))
					status = true;
			}
		}
	}
	
	if (selected)
		tsc = document.all(iddiv).all.tags('SELECT');
	else if (lastSelected[0] != null)
		tsc = lastSelected[0].all.tags('SELECT');
	else
		tsc = null;

	if (tsc != null)
	{
		for (var x = 0; x < tsc.length; x++)
		{
			if (disOrEna(tsc[x], true))
				status = true;
		}
	}
	
	if (selected)
		tsc = document.all(iddiv).all.tags('TEXTAREA');
	else if (lastSelected[0] != null)
		tsc = lastSelected[0].all.tags('TEXTAREA');
	else
		tsc = null;
	
	if (tsc != null)
	{
		for (var x = 0; x < tsc.length; x++)
		{
			if (disOrEna(tsc[x], true))
				status = true;
		}
	}
	
	if (status)
		disOrEnaButton(obj.name);
}

var xyz = 0;

function disOrEna(obj, status)
{
/*
	var readOnly = obj.getAttribute('READONLY');
	
	if (readOnly == null)
		readOnly = false;
	else if (readOnly.toString() == "")
		readOnly = true;
		
	if (!readOnly)
	{
		if (status == 1)
			obj.disabled = true;
		else
			obj.disabled = false;
	}
*/

	if (obj.className.toLowerCase() != 'readonly')
	{
		obj.disabled = status;
		return true;
	}
	
	return false;
}
			
function disOrEnaButton(trans1)
{
	var btn = document.all.button;
	trans = trans1.toUpperCase();
			
	if (trans == 'ABORT' || trans == 'SAVE')
	{
		for (var x = 0; x < btn.length; x++)
		{
			if (btn[x].name == 'close' || btn[x].name == 'add' || btn[x].name == 'delete' || btn[x].name == 'modify' || btn[x].name == 'retrieve' || btn[x].name == 'print')
				btn[x].disabled = false;
			else
				btn[x].disabled = true;
		}

		curMode = 0;
	}
	else if (trans == 'MODIFY')
	{
		for (var x = 0; x < btn.length; x++)
		{
			if (btn[x].name == 'close' || btn[x].name == 'save' || btn[x].name == 'abort')
				btn[x].disabled = false;
			else
				btn[x].disabled = true;
		}

		curMode = 3;
	}
	else if (trans == 'ADD')
	{
		for (var x = 0; x < btn.length; x++)
		{
			if (btn[x].name == 'close' || btn[x].name == 'add' || btn[x].name == 'save' || btn[x].name == 'delete' || btn[x].name == 'abort')
				btn[x].disabled = false;
			else
				btn[x].disabled = true;
		}

		curMode = 2;
	}
	else if (trans == 'VIEW')
	{
		for (var x = 0; x < btn.length; x++)
		{
			if (btn[x].name == 'close' || btn[x].name == 'print')
				btn[x].disabled = false;
			else
				btn[x].disabled = true;
		}

		curMode = 0;
	}
	
	btn = document.all.tab;
	
	if (btn != null)
	{
		if (trans == 'ABORT' || trans == 'SAVE')
		{
			for (var x = 0; x < btn.length; x++)
				btn[x].disabled = false;
		}
		else if (trans == 'MODIFY' || trans == 'ADD')
		{
			for (var x = 0; x < btn.length; x++)
				btn[x].disabled = true;
		}
	}
}


function addNewRow(idDiv, idTable, idFrame, actCell,iFrame)
{
	try
	{
		
		if (iFrame==true)
			var objTable = document.frames(idFrame).document.all(idTable);

		else
			var objTable = document.all(idTable);
		

		var rows = objTable.rows;
		

		var nextNo;
			
		if (rows.length > 0)
			nextNo = parseInt(rows.length) + 1;
		else
			nextNo = 1;

//		var scrollStatus = configureScroll(idDiv, 2);

		
		var newRow = objTable.insertRow();
		
		
		var totalCells = objTable.all.tags('COL').length;
		
			
		for (var x = 0; x < totalCells; x++)
			newRow.insertCell();
		
		
		addSyntax(newRow, nextNo);



		if (iFrame==true)
			document.frames(idFrame).resetSelected();
		else
			resetSelected();

        newRow.click();
        //lastSelected[0] = newRow;


		objTable.outerHTML = objTable.outerHTML;
		//objTable.refresh();

    /*
//		if (scrollStatus == 0 && configureScroll(idDiv, 2) == 1)
//			configureScroll(idDiv, 1);
              */

		if (iFrame==true)
			var objTable = document.frames(idFrame).document.all(idTable);

		else
			var objTable = document.all(idTable);

		rows = objTable.rows;

		rows(rows.length - 1).click();
		rows(rows.length - 1).scrollIntoView(true);
		
		/*var children = rows(rows.length - 1).cells(actCell).children;
		
		for (var y = 0; y < children.length; y++)
		{
			if (!children(y).disabled && (children(y).tagName == 'INPUT' || children(y).tagName == 'SELECT' || children(y).tagName == 'TEXTAREA'))
			{
				children(y).focus();
				
				if (children(y).tagName != 'SELECT')
					children(y).select();
					
				break;
			}
		}*/

		//resetSelected();
	}
	catch (err)
	{
		alert (err.message);
	}
}



function getSelected()
{
	return lastSelected;
}


function deleteRow(idDiv, idTable, idFrame, iFrame, updateNo)
{

	if (iFrame==true)
		getlastSelected = document.frames(idFrame).lastSelected;
	else
		getlastSelected = lastSelected;


	//if (lastSelected[0] != null)
	if (getlastSelected[0] != null)
	{
		
		if (iFrame==true)
			var objTable = document.frames(idFrame).document.all(idTable);
		else
			var objTable = document.all(idTable);

		//var position = lastSelected[0].rowIndex;
		var position = getlastSelected[0].rowIndex;
	
		//if (checkDelete(lastSelected[2], lastSelected[3]))
		if (checkDelete(getlastSelected[2], getlastSelected[3]))
		{
			var rows = objTable.rows;
				
			position = parseInt(position);
			
//			changeID();
			
//			if (updateNo)
//			{
//				var counter = 1;
//				
//				for (var x = 0; x < rows.length; x++)
//				{
//					if (x != position)
//					{
//						if (rows(x).cells(0).children(0) != null)
//							rows(x).cells(0).children(0).value = counter;
//						else
//							rows(x).cells(0).innerText = counter;
//							
//						counter++;
//					}
//				}
//			}
			
			//var scrollStatus = configureScroll(idDiv, 2);

			
			objTable.deleteRow(position);
			

			//objTable.refresh();
			objTable.outerHTML = objTable.outerHTML;

			if (iFrame==true)
				document.frames(idFrame).resetSelected();
			else
				resetSelected();

//			resetSelected();

			//if (scrollStatus == 1 && configureScroll(idDiv, 2) == 0)
			//	configureScroll(idDiv, 0);
		}
	}
}


/* use style -> text-transform: uppercase

var character = new Array('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z');
			
function upperCase(obj)
{
	if (obj.value.length < obj.maxLength)
	{
		var key = window.event.keyCode;

		if (key >= 65 && key <= 90)
		{
			window.event.returnValue = false;
			obj.value += character[key - 65];
		}
	}
}
*/



