	<HTML>
  <HEAD>
    <META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=iso-8859-1">
    <TITLE>MEDiSafe</TITLE>
    <CENTER>
  </HEAD>
  <script>
    var win;

    function recheckchild() {
        if (win.closed) {
            openLoginScreen();	
        }else {
            win.focus();
        }
    }

    function openLoginScreen() {
        win = window.open('zkpages/login.zul','MEDISAFEMAINWINDOW','width=790,height=544,left=0,top=0,scrollbars=no,resizeable=no,toolbar=no,titlebar=no,status=no,max=no',true);
        try {
            win.resizeTo(screen.availWidth,screen.availHeight);
        }catch(exception) {
        }
        win.focus();
        window.opener = top;
        window.close();

    }
  </script>

  <BODY onload="openLoginScreen()" >
    
	<A HREF="#" onclick="recheckchild();">
        <U><IMG alt="MEDiSave Hospital Management System" src="image/java.gif" border="0"></U><HR>
    </A>
    <BR>
  </BODY>
</HTML>
