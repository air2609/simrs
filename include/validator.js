var VAP = new Array();

var minYear=1900;

var maxYear=9999;

var defaultFilterSrc='[a-zA-Z0-9 \\.\\/\\*\\-\\(\\)\\$\\@\\,\\?\\;\\:\\[\\]\\{\\}\\_\\!\\#\\^\\=\\~]';

var defaultFilter = new RegExp(defaultFilterSrc);

var VC_FLOATDECIMALSEPARATOR = '.';

var VC_FLOATGROUPSEPARATOR = ',';

var dtCh= "/";

var _xPos = 0;

var _curPos = 0;

var _textSelect = false;

var daysInMonth = [31,29,31,30,31,30,31,31,30,31,30,31];

var codes = [190,191];

var keys = ['.','/'];

var _isError = false;

var _isLoop = false;

/*
    home = 36
    shift = 16
    end = 35
    ctrl = 17
    tab = 9
*/

var spKeyCode = [9,16,17,35,36]

/*
    delete = 46
    backspace = 8
*/
var delKeyCode = [46,8];

/*
    Characters used in regualar ekspresion. To test this characters, must include escape character.
    # = 35
    $ = 36
    % = 37
*/
var numberSpChar = [35,36,37];



function VA_req(control,fieldname,datatype,maxlen, frameName, type, noselect) {
    VM_setmandatory(control);
    V = VA_reg(control,fieldname,datatype,maxlen, frameName, type, noselect);
    return V;
}

function VA_unreg(control) {
   var i = 0;
   for (; i<VAP.length; i++) {
      if (VAP[i].control == control) {
         VAP[i].control = null;
      }
   }
}

function VA_unregRow(objRow) {
   var arrTagName = new Array();
   arrTagName[0] = "INPUT";
   arrTagName[1] = "SELECT";
   for (var i=0; i<arrTagName.length; i++) {
     var arrInput = objRow.getElementsByTagName(arrTagName[i]);
     for (var j=0; j<arrInput.length; j++) {
         VA_unreg(arrInput[j]);
      }
   }
}

function VM_setmandatory(ctl) {
   ctl.required = true;
   ctl.className = "mandatory";
}

function newVAP() {
    var i = VAP.length;

    VAP[i] = new Object();

    return VAP[i];
}

function showMessage(errmsg) {
    window.alert(errmsg)
}

function VA_reg(control,fieldname,datatype,params, frameName, type, noselect) {
    if (control == null) {
        showMessage('undefined control !');
        return;
    }

    if (!control.form) {
       showMessage('Form control not found ('+control[0].name+','+fieldname+') !');
       return;
    }

    if (datatype == null) datatype = 'string';

    var V=newVAP();
    V.control = control;
    V.form = control.form;
    V.frameName = frameName;
    V.type = type;
    V.fieldname = fieldname;
    V.controlname = control.name;
    V.noselect = noselect;

    control.validateobject = V;
    control.setfilter = VH_setfilter;
    control.setrange = VH_setrange;
    control.notequalcontrol = VH_notequalcontrol;
    control.nozero = VH_nozero;
    control.fieldname = fieldname;
    control.validate = false;
    control.first = true;

    if (control.tabIndex == 0)  {
        control.tabIndex  = VAP.length;
    }

    //events use in validator
    //onfocus
    //onkeypress
    //onkeydown
    //onclick
    //onpaste

    if (V.control.onfocus != VM_validateonfocus) {
        V.control.focusevent = control.onfocus;
        V.control.onfocus = VM_validateonfocus;
    }

    if (V.control.onkeypress != VH_keypress) {
        V.control.keypressevent = V.control.onkeypress;
        V.control.onkeypress = VH_keypress;
    }

    if (V.control.onkeydown != VH_keydown) {
        V.control.keydownevent = V.control.onkeydown;
        V.control.onkeydown = VH_keydown;
    }

    V.control.onpaste = VH_paste;

    VH_setdatatype(V,datatype,params);
    return V;
}

function VM_isrequired(control) {
   return control.getAttribute("required") != null;
}

function VH_setdatatype(V,dt,params) {
    if (dt.substr(0,5) == 'float') {
        dtp = dt.substr(5);
        var pf = dtp.split('.');

        _unit = parseInt(pf[0] - pf[1]);
        _prec = parseInt(pf[1]);

        V.unit = _unit;
        V.prec = _prec;

        dt = "number";


        V.RE = new RegExp('(^((([1-9])([0-9]{0,' + (_unit - 1) + '}))|(0))?(\\.([0-9]{0,' + _prec + '}))?$)');

        params = _unit + parseInt((_unit - 1)/3) + 1 + _prec;

    } else if (dt == "integer") {
        V.unit = params;
        V.prec = 0;

        dt = "number";


        V.RE = new RegExp('^((([1-9])([0-9]{0,' + (params - 1) + '}))|(0))$');

        params = params + parseInt((params - 1)/3);

    } else if (dt == "datesequence") {
        V.RE = new RegExp('^([0-9]{1,2})/?((([0-9]{1,2})/?)?)((([0-9]{1,4})/?)?)$');

        V.control2 = params;
        V.fieldname2 = getfieldname(params);
        params = 10;
    } else if (dt == "yearmonth") {
        V.RE = new RegExp('^([0-9]{1,4})/?((([0-9]{1,2})/?)?)$');
        params = 7;
    } else if (dt == "monthyear") {
        V.RE = new RegExp('^([0-9]{1,2})/?((([0-9]{1,4})/?)?)$');
        params = 7;
    } else if (dt == "date") {
        V.RE = new RegExp('^([0-9]{1,2})/?((([0-9]{1,2})/?)?)((([0-9]{1,4})/?)?)$');
        params = 10;
    } else if (dt == "time") {
        V.RE = new RegExp('^((([0-9]{1,2}):?)?)(([0-9]{1,2})?)$');
        params = 5;
    } else if (dt == "datetime") {
        V.RE = new RegExp('^([0-9]{1,2})/?((([0-9]{1,2})/?)?)((([0-9]{1,4})/?)?)\\s?((([0-9]{1,2}):?)?)(([0-9]{1,2})?)$');
        params = 16;
    } else if (dt == "contsize") {
        V.RE = new RegExp('^(2|4)0?$');
        params = [2,2];
    } else if (dt == "daymonth") {
        V.RE = new RegExp('^([0-9]{1,2})/?((([0-9]{1,2})/?)?)$');
        params = 7;
    }


    if (dt == "number" || dt == "contsize") {
        if (VM_isrequired(V.control)) {
            V.control.className = "mandatory_number";
        }else {
            V.control.className = "number";
        }
    }

    V.datatype = dt;

    if (params != null) {
        VH_setmaxlen(V,params);
    }
}

function VH_setmaxlen(V,maxlen) {
    if (maxlen.length == null) {
        V.minlen = 0; V.maxlen = maxlen;
    } else {
        V.minlen=maxlen[0];
        V.maxlen=maxlen[1];
    }

    //assign max length to control
    if (V.control.maxLength != null) {
        V.control.maxLength = V.maxlen;
    } else {
        V.control.setAttribute('maxlength',V.maxlen);
    }
}

function getEvent(V) {
    var myEvent = null;

    if (V.frameName == null) {
        myEvent = event;
    } else {
        myEvent = document.frames[V.frameName].event;
    }

    return myEvent;
}

function getControl(V) {
    var control = null;

    if (V.frameName == null) {
        control = V.control;
    }else {
        //control = document.frames[V.frameName].document.all[V.controlname];
        return V.control;
    }

    return control;
}

function VH_keypress(){
    V = this.validateobject;
    var isNumber = V.datatype == "number" ? true : false;

    var myEvent = getEvent(V);

    var _keyCode = myEvent.keyCode;

    if (myEvent.ctrlKey) {
        return;
    }

    for (var i=spKeyCode.length; --i>=0; ) {
        if (_keyCode == spKeyCode[i] && !myEvent.shiftKey) {
            return;
        }
    }

    this.validate = true;

    var sKey=String.fromCharCode(_keyCode);
    if (this.type != "file" && this.type != "password" && V.datatype != "nochange" && this.noUpper == null) {
        myEvent.keyCode = sKey.toUpperCase().charCodeAt(0);
        sKey = sKey.toUpperCase();
    }

    if (sKey != '\r') {
        if (V.maxlen != null) {
            if (this.value.length > V.maxlen) {
                myEvent.returnValue = false;
            }
        }

        var RE = V.RE;
        if (RE) {
            if (isNumber) {
                for (var i=numberSpChar.length; --i>=0; ) {
                    if (_keyCode == numberSpChar[i]) {
                        myEvent.returnValue = false;
                        return;
                    }
                }
            }

            var Sr = valueprediction(this,sKey, false, V.frameName);

            var _oldRelPos = this.value.length - _curPos;

            if (isNumber) {
                Sr = VH_removeDecimalSep(Sr);
            }

            if (!RE.test(Sr)) {
                myEvent.returnValue=false;
                return false;
            }

            if (_textSelect) {
                myEvent.returnValue = true;
                return;
            }

            if (isNumber) {
                if (V.nozero) {
                    if (isZero(V.prec, Sr)) {
                        myEvent.returnValue = false;
                        return false;
                    }
                }

                VH_numericFormat(this, Sr);

                var txtRng = this.createTextRange();
                txtRng.move("character",(this.value.length - _oldRelPos));
                txtRng.select();
                myEvent.returnValue = false;
                this.changed = true;
            }
        }
    }
    if (V.control.keypressevent != null) {
        V.control.keypressevent();
    }
    return true;
}

function isZero(_prec, _value) {
    if (_prec == 0) {
        return false;
    }
    var pos = _value.indexOf(".");
    var value = pos == -1 ? _value : _value.substr(0,pos);
    var scale = pos == -1 ? "" : _value.substr(pos+1);

    if (scale.length < _prec){
        return false;
    } else if (value.length == 1 && parseInt(value) == 0 && removeZero(scale) == "") {
        return true;
    }
    return false;
}

function removeZero(_val){
    var _newVal = "";
    var _char = null;

    for (var i=_val.length; --i>=0; ){
        _char = _val.charAt(i);
        if (_char != "0") {
            _newVal = _char + _newVal;
        }
    }
    return _newVal;
}

function VH_keydown(){
    V = this.validateobject;

    var myEvent = getEvent(V);

    var _keyCode = myEvent.keyCode;

    if (myEvent.ctrlKey) {
        return;
    }

    for (var i=spKeyCode.length; --i>=0; ) {
        if (_keyCode == spKeyCode[i]) {
            return;
        }
    }

    this.validate = true;

    var sKey=String.fromCharCode(_keyCode);

    if (sKey != '\r') {
        var RE = V.RE;
        if (RE) {
            var isNumber = V.datatype == "number" ? true : false;
            if (_keyCode == 46 || _keyCode == 8) {

                var isBackSpace = true;
                if (_keyCode == 46) {
                    isBackSpace = false;
                }

                var Sr = valueprediction(V.control,sKey, true, V.frameName, isBackSpace);
                if (_textSelect) {
                    myEvent.returnValue = true;
                } else if (isNumber) {
                    var _dif = this.value.length - _curPos;

                    VH_numericFormat(this,VH_removeDecimalSep(Sr));

                    var txtRng = this.createTextRange();

                    txtRng.move("character",(this.value.length - _dif));
                    txtRng.select();

                    myEvent.returnValue = false;
                }
            }
        }
    }
    if (this.keydownevent != null) {
        this.keydownevent();
    }
    return true;
}


function valueprediction(ctl, sKey, isDelete, frameName, isBackSpace) {
    var V = ctl.validateobject;
    var cr = null;
    if (frameName == null) {
        cr = document.selection.createRange();
    } else {
        cr = document.frames[frameName].document.selection.createRange();
    }

    var seltext = cr.text;

    if (V.defaultrange == null) {
      V.defaultrange = cr.duplicate();
      V.defaultrange.expand('textedit');
    }

    var dr = V.defaultrange;
    _xPos = 0;

    if (seltext != '') {
        _textSelect = true;
        //return;
    } else {
        _textSelect = false;
    }

    while (dr.compareEndPoints('StartToStart',cr)<0) {
      cr.move('character',-1);
      _xPos++;
    }

    var src = ctl.value;
    if (isDelete) {
        if (isBackSpace) {   //delete first character
            src =  src.substring(0,_xPos-1)+src.substring(_xPos+seltext.length,src.length);
            _curPos = _xPos;
        } else {
            src =  src.substring(0,_xPos)+src.substring(_xPos+seltext.length+1,src.length);
            _curPos = _xPos+1;
        }
    } else {
        src =  src.substring(0,_xPos)+sKey+src.substring(_xPos+seltext.length,src.length);
        _curPos = _xPos;
    }
    return src;
}

function VM_validateFrame(frameName, fName) {
    var ctl = null;
    var _refactor = false;
    var V = null;
    for (var i=0;i<VAP.length;i++) {
        V = VAP[i];
        ctl = getControl(V);

        if (V.frameName == frameName && ctl != null && !ctl.disabled && ctl.parentElement != null) {
            if (!VM_validate(V)) {
               if (V.type == null) {    //detail
                    selected(getTR(ctl));
               } else {
                    if (V.frameName == null) {  //object reside at header
                        selected(document.all[V.type]);
                    } else {
                        selected(document.frames[V.frameName].document.all[V.type]);
                    }
               }
               if (ctl.tagName == "SELECT") {
                    if (ctl.value == '' && ctl.options.length > 0) {
                        ctl.options[0].selected = true;
                    }
               } else {
                    ctl.select();
               }
               if (fName != null) {
                    eval(fName);
               }
               setHorizontalScroll(ctl);
               ctl.focus();
               ctl.validate = true;
               _isError = true;
               return false;
            }
        } else if (ctl == null || ctl.parentElement == null) {
            _refactor = true;
        }
    }
    if (_refactor) {
        refactor();
    }

    return true;
}

function setHorizontalScroll(control) {
    var tr = getTR(control);
    var tmp = tr.parentElement;

    while (tmp != null && tmp.tagName != "DIV") {
        tmp = tmp.parentElement;
    }

    var _left = control.parentElement.offsetLeft;
    if (tmp != null && (_left + 100) > (tmp.offsetWidth + tmp.scrollLeft)) {
            tmp.scrollLeft = _left;
    }
}

function VM_dovalidate(F) {
    var ctl = null;
    var _refactor = false;
    var V = null;
    for (var i=0;i<VAP.length;i++) {
        V = VAP[i];
        ctl = getControl(V);

        if (V.form == F && ctl != null && !ctl.disabled && ctl.parentElement != null) {
            if (!VM_validate(V)) {
               setHorizontalScroll(ctl);
               if (ctl.tagName == "SELECT") {
                    if (ctl.value == '' && ctl.options.length > 0) {
                        ctl.options[0].selected = true;
                    }
               } else {
                    ctl.select();
               }
               ctl.focus();
               ctl.validate = true;
               _isError = true;
               return false;
            }
        } else if (ctl == null || ctl.parentElement == null) {
            _refactor = true;
        }
    }
    if (_refactor) {
        refactor();
    }

    return true;
}

function VD_validate(Vexpression,control,errmsg) {
    if (Vexpression)  return true;
    showMessage(errmsg)
    return false;
}

function VM_validate(V) {

    var control = getControl(V);

    if (control == null) {
        V.control = null;
        return true;
    }

    var val = control.value;
    val = val.trim();
    control.value = val;

    if (val != null) {
        if (control.required == null && val == '') return true;

        if (!VD_validate((val != ''),control,'Field {0} cannot be emptied'.replace("{0}", V.fieldname))) return false;
        if (!control.validate) {
            if (control.first) {
                control.first = false;
            } else {
                return true;
            }
        }

        if (V.minlen != null) {
            var _len = V.maxlen;
            if (V.minlen == _len) {
                if (V.datatype == "contsize") {
                    if (! ( VD_validate( ( (new String(val)).length == _len),control,'TEST MSG {0} 33') ) ) {
                        return false;
                    }
                } else if (! ( VD_validate( ((new String(val)).length == _len),control,'TEST MSG {0} 105'.replace("{0}", V.fieldname).replace("{1}",_len) ) ) ) {
                    return false;
                }
            } else if (V.datatype == "string" && ! ( VD_validate( ((new String(val)).length >= V.minlen && (new String(val)).length <= _len),control,'TEST MSG {0} 154'.replace("{0}", V.fieldname).replace("{1}",V.minlen).replace("{2}",_len) ) ) ) {
                return false;
            }
        }

        if (V.RE != null) {
            var _val = control.value;
            if (V.datatype == "number") {
                _val = VH_removeDecimalSep(_val);
                if (!V.RE.test(_val)) {
                    if (parseFloat(_val) == 0) {
                        showMessage('TEST MSG {0} 94'.replace("{0}",V.fieldname).replace("{1}","zero (0)") );
                    } else {
                        //enabling user to outfocus if value -1
                        //medisave inventory requirement
                        return true;
                        //showMessage('testignngsjngrjng {0} 97'.replace("{0}",V.fieldname));
                    }
                    return false;
                }
            } else {
/*                if (!V.RE.test(_val)) {
                    showMessage("Special Character are not allowed for filed "+V.fieldname);
                    return false;
                } */
            }
        }

        if (V.datatype == "number") {
            if (control.noformat == null) {
                VH_formatScale(control);
            }
            var _orgValue = VH_removeDecimalSep(control.value);
            if (V.nozero) {
                if (isZero(V.prec, _orgValue)) {
                    showMessage('TEST MSG {0} 94'.replace("{0}",V.fieldname).replace("{1}","zero (0)") );
                    return false;
                }
            }
            VH_numericFormat(control, _orgValue);
        }

        if (V.datatype == "date" || V.datatype == "datesequence") {
            if ( !( VD_datevalid(control,'Invalid input for {0}'.replace("{0}",V.fieldname).replace("{1}","[dd/mm/yyyy]") ) ) ) return false;
        }

        if (V.datatype == "datesequence") {
           if ( !(VD_datesequencevalid(control,V.control2,'Invalid input for {0} '.replace("{0}",V.fieldname).replace("{1}",V.fieldname2) ) ) ) return false;
        }

        if (V.datatype == "yearmonth") {
           if (! ( VD_yearmonthvalid(control,'Invalid input for {0}'.replace("{0}",V.fieldname).replace("{1}","[yyyy/mm]") ) ) ) return false;
        }

        if (V.datatype == "monthyear") {
           if (! ( VD_monthyearvalid(control,'Invalid input for {0} '.replace("{0}",V.fieldname).replace("{1}","[mm/yyyy]") ) ) ) return false;
        }

        if (V.datatype == "datetime") {
           if ( !(VD_datetimevalid(control, 'Invalid input for {0}'.replace("{0}",V.fieldname).replace("{1}","[dd/mm/yyyy hh:mm]") ) ) ) return false;
        }

        if (V.datatype == "daymonth") {
           if ( !(VD_daymonthvalid(control, 'Invalid input for {0}'.replace("{0}",V.fieldname).replace("{1}","[dd/mm]") ) ) ) return false;
        }

        if (control.min != null) {
            min = control.min;
            if (V.datatype == "number") {
                var iMin = VH_removeDecimalSep(min);
                if (control.include != null) {
                    if (! ( VD_validate((parseFloat(VH_removeDecimalSep(control.value)) >= parseFloat(iMin)), control, 'TEST MSG {0} 95'.replace("{0}",V.fieldname).replace("{1}",min) ) ) ) return false;
                } else {
                    if (! ( VD_validate((parseFloat(VH_removeDecimalSep(control.value)) > parseFloat(iMin)), control, 'TEST MSG {0} 94'.replace("{0}",V.fieldname).replace("{1}",min) ) ) ) return false;
                }
            } else if (V.datatype == "date" || V.datatype == "datesequence") {
                if (control.include != null) {
                    if (! ( VD_validate((isDate(control.value) && compareDate(control.value,min) >= 0), control, 'TEST MSG {0} 95'.replace("{0}",V.fieldname).replace("{1}",min) ) ) ) return false;
                } else {
                    if (! ( VD_validate((isDate(control.value) && compareDate(control.value,min) > 0), control, 'TEST MSG {0} 94'.replace("{0}",V.fieldname).replace("{1}",min) ) ) ) return false;
                }
            }
        }

        if (control.max != null) {
            max = control.max;
            if (V.datatype == "number") {
                var iMax = VH_removeDecimalSep(max);
                if (control.include != null) {
                    if (! ( VD_validate((parseFloat(VH_removeDecimalSep(control.value)) <= parseFloat(iMax)), control, 'TEST MSG {0} 138'.replace("{0}",V.fieldname).replace("{1}",max) ) ) ) return false;
                } else {
                    if (! ( VD_validate((parseFloat(VH_removeDecimalSep(control.value)) < parseFloat(iMax)), control, 'TEST MSG {0} 137'.replace("{0}",V.fieldname).replace("{1}",max) ) ) ) return false;
                }
            }else if (V.datatype == "date" || V.datatype == "datesequence") {
                if (control.include != null) {
                    if (! ( VD_validate((isDate(control.value) && compareDate(control.value,max) <= 0), control, 'TEST MSG {0} 138'.replace("{0}",V.fieldname).replace("{1}",max) ) ) ) return false;
                } else {
                    if (! ( VD_validate((isDate(control.value) && compareDate(control.value,max) < 0), control, 'TEST MSG {0} 137'.replace("{0}",V.fieldname).replace("{1}",max) ) ) ) return false;
                }
            }
        }

        if (control.notequalcontrol == true) {
            if (! (VD_validate((control.value != V.control3.value), control, 'TEST MSG {0} 93'.replace("{0}",V.fieldname).replace("{1}",V.fieldname3) ) ) ) return false;
        }
   }

   control.validate = false;
   if (control.changed) {
        if (control.onchange != null) {
            control.onchange();
        }
   }
   return true;
}

function VD_notnull(control,errmsg) {
    return VD_validate((control.value != ''),control,errmsg);
}

function daysInFebruary (year){
    return (((year % 4 == 0) && ( (!(year % 100 == 0)) || (year % 400 == 0))) ? 29 : 28 );
}

function isYearMonth(ctl, stYearMonth) {
   var pos1 = stYearMonth.indexOf(dtCh);
   var year = stYearMonth.substr(0,pos1);
   var month = stYearMonth.substr(pos1+1,stYearMonth.length);

   if (month.length == 1) {
      month = "0" + month;
   }

   if (year.length != 4 || month.length != 2) {
      return false;
   }

   if (year < minYear || year > maxYear) {
      return false;
   }

   var monthInt = month;

   if (month.charAt(0) == '0') {
      monthInt = month.charAt(1);
   }

   if (parseInt(monthInt) < 1 || parseInt(monthInt) > 12) {
      return false;
   }

   ctl.value = year + dtCh + month;
   return true;
}

function isDayMonth(ctl, stDayMonth) {
   var pos1 = stDayMonth.indexOf(dtCh);
   var day = stDayMonth.substr(0,pos1);
   var month = stDayMonth.substr(pos1+1,stDayMonth.length);

   if (day.length == 1) {
      day = "0" + day;
   }

   if (month.length != 2 || day.length != 2) {
      return false;
   }

   var dayInt = day;

   if (day.charAt(0) == '0') {
      dayInt = day.charAt(1);
   }

   if (parseInt(dayInt) < 1 || parseInt(dayInt) > 31) {
      return false;
   }

   var monthInt = month;

   if (month.charAt(0) == '0') {
      monthInt = month.charAt(1);
   }

   if (parseInt(monthInt) < 1 || parseInt(monthInt) > 12) {
      return false;
   }

   ctl.value = day + dtCh + month;
   return true;
}

function isMonthYear(ctl, stMonthYear) {
   var pos1 = stMonthYear.indexOf(dtCh);
   var month = stMonthYear.substr(0,pos1);
   var year = stMonthYear.substr(pos1+1,stMonthYear.length);

   if (month.length == 1) {
      month = "0" + month;
   }

   if (year.length != 4 || month.length != 2) {
      return false;
   }

   if (year < minYear || year > maxYear) {
      return false;
   }

   var monthInt = month;

   if (month.charAt(0) == '0') {
      monthInt = month.charAt(1);
   }

   if (parseInt(monthInt) < 1 || parseInt(monthInt) > 12) {
      return false;
   }

   ctl.value = month + dtCh + year;
   return true;
}



function isDate(dtStr){
	var pos1=dtStr.indexOf(dtCh)
	var pos2=dtStr.indexOf(dtCh,pos1+1)
	var strDay=dtStr.substring(0,pos1)
	var strMonth=dtStr.substring(pos1+1,pos2)
	var strYear=dtStr.substring(pos2+1)
	strYr=strYear

	if (strDay.charAt(0)=="0" && strDay.length>1) strDay=strDay.substring(1)
	if (strMonth.charAt(0)=="0" && strMonth.length>1) strMonth=strMonth.substring(1)

	for (var i = 1; i <= 3; i++) {
		if (strYr.charAt(0)=="0" && strYr.length>1) strYr=strYr.substring(1)
	}

	month=parseInt(strMonth)
	day=parseInt(strDay)
	year=parseInt(strYr)
	if (pos1==-1 || pos2==-1){
		return false
	}
	if (strMonth.length<1 || month<1 || month>12){
		return false
	}
	if (strDay.length<1 || day<1 || day>31 || (month==2 && day>daysInFebruary(year)) || day > daysInMonth[month-1]){
		return false
	}

	if (strYear.length != 4 || year==0 || year<minYear || year>maxYear){
		return false
	}
	if (dtStr.indexOf(dtCh,pos2+1)!=-1 || isInteger(stripCharsInBag(dtStr, dtCh))==false){
		return false
	}
    return true
}

function isTime(dtStr){
	var pos1=dtStr.indexOf(":")
	var stHour=dtStr.substring(0,pos1)
	var stMinute=dtStr.substring(pos1+1)

	if (stHour.length != 2 || stMinute.length != 2) {
	    return false;
	}

	if (stHour.charAt(0) == '0') {
        stHour = stHour.charAt(1);
	}

	if (stMinute.charAt(0) == '0') {
        stMinute = stMinute.charAt(1);
	}

	if (parseInt(stHour) < 0 || parseInt(stHour)> 23) {
	    return false;
	}

	if (parseInt(stMinute) < 0 || parseInt(stMinute)> 59) {
	    return false;
	}

    return true
}

function isInteger(s){
	var i;
    for (i = 0; i < s.length; i++){
        var c = s.charAt(i);
        if (((c < "0") || (c > "9"))) return false;
    }
    return true;
}

function stripCharsInBag(s, bag){
	var i;
    var returnString = "";
    for (i = 0; i < s.length; i++){
        var c = s.charAt(i);
        if (bag.indexOf(c) == -1) returnString += c;
    }
    return returnString;
}

function VD_datevalid(control,errmsg) {
    sDate = control.value;

    if (sDate.indexOf(dtCh)==-1) {
        sDate=sDate.substr(0,2) + "/" + sDate.substr(2,2) + "/" + sDate.substr(4,4);
    }

    var r = ( VD_notnull(control,errmsg) && VD_validate(isDate(sDate),control,errmsg) );

    if (r) {
        var tmp = sDate.split("/");
        for (var i=0; i<tmp.length; i++) {
            if (tmp[i].length == 1) {
                tmp[i] = '0' + tmp[i];
            }
        }
        sDate = tmp[0] + '/' + tmp[1] + '/' + tmp[2];
        control.value = sDate;
    }

    return r;
}

function VD_timevalid(control,errmsg) {
    sTime = control.value;

    if (sTime.indexOf(":")==-1) {
        sTime=sTime.substr(0,2) + ":" + sTime.substr(2,2);
    }

    var r = ( VD_notnull(control,errmsg) && VD_validate(isTime(sTime),control,errmsg) );

    if (r) control.value = sTime;

    return r;
}

function VD_yearmonthvalid(control, errmsg) {
   sYearMonth = control.value;
   var year, month;
   if (sYearMonth.indexOf(dtCh) == -1) {
      sYearMonth = sYearMonth.substr(0,4) + "/" + sYearMonth.substr(4);
   }

   var r = ( VD_notnull(control,errmsg) && VD_validate(isYearMonth(control, sYearMonth),control,errmsg) );

   return r;
}

function VD_monthyearvalid(control, errmsg) {
   sMonthYear = control.value;
   var year, month;

   if (sMonthYear.indexOf(dtCh) == -1) {
      if (sMonthYear.length < 5) {
        showMessage(errmsg);
        control.focus();
        return false;
      }

      if (sMonthYear.length == 5) {
        sMonthYear = sMonthYear.substr(0,1) + "/" + sMonthYear.substr(1);
      }else {
        sMonthYear = sMonthYear.substr(0,2) + "/" + sMonthYear.substr(2);
      }
   }

   var r = ( VD_notnull(control,errmsg) && VD_validate(isMonthYear(control, sMonthYear),control,errmsg) );

   return r;
}

function VD_daymonthvalid(control, errmsg) {
   sDayMonth = control.value;
   var day, month;

   if (sDayMonth.indexOf(dtCh) == -1) {
      if (sDayMonth.length < 3) {
        showMessage(errmsg);
        control.focus();
        return false;
      }

      if (sDayMonth.length == 3) {
        sDayMonth = sDayMonth.substr(0,1) + "/" + sDayMonth.substr(1);
      }else {
        sDayMonth = sDayMonth.substr(0,2) + "/" + sDayMonth.substr(2);
      }
   }

   var r = ( VD_notnull(control,errmsg) && VD_validate(isDayMonth(control, sDayMonth),control,errmsg) );

   return r;
}

function VD_datetimevalid(control, errmsg) {
    sDateTime = control.value;
    var sDate;
    var sTime;
    var index = sDateTime.indexOf(' ');
    if (index ==-1) {
        showMessage(errmsg);
        return;
    }
    sDate = sDateTime.substr(0,index);
    sTime = sDateTime.substr(index+1);

    if (sDate.indexOf(dtCh)==-1) {
        sDate=sDate.substr(0,2) + "/" + sDate.substr(2,2) + "/" + sDate.substr(4,4);
    }

    var r1 = ( VD_notnull(control,errmsg) && VD_validate(isDate(sDate),control,errmsg) );
    if (sTime.indexOf(":")==-1) {
        sTime=sTime.substr(0,2) + ":" + sTime.substr(2,2);
    }

    var r2 = ( VD_notnull(control,errmsg) && VD_validate(isTime(sTime),control,errmsg) );

    if (r1 && r2) control.value = sDate+" "+sTime;

    return (r1 && r2);
}

function datestrtointeger(dtStr) {
	var pos1=dtStr.indexOf(dtCh)
	var pos2=dtStr.indexOf(dtCh,pos1+1)
	var strDay=dtStr.substring(0,pos1)
	var strMonth=dtStr.substring(pos1+1,pos2)
	var strYear=dtStr.substring(pos2+1)
	strYr=strYear
	if (strDay.charAt(0)=="0" && strDay.length>1) strDay=strDay.substring(1)
	if (strMonth.charAt(0)=="0" && strMonth.length>1) strMonth=strMonth.substring(1)
	for (var i = 1; i <= 3; i++) {
		if (strYr.charAt(0)=="0" && strYr.length>1) strYr=strYr.substring(1)
	}
	month=parseInt(strMonth)
	day=parseInt(strDay)
	year=parseInt(strYr)

    var d = day + (month*31) + (year*31*12);

    return d;
}

function compareDate(dtStr1, dtStr2) {
   var dt1 = datestrtointeger(dtStr1);
   var dt2 = datestrtointeger(dtStr2);

    if (dt1 > dt2) return 1; else
    if (dt1 < dt2) return -1; else
        return 0;
}

function VD_datesequencevalid(ctrl1, ctrl2, errmsg) {
   return VD_validate(compareDate(ctrl1.value, ctrl2.value)>=0, ctrl1, errmsg);
}

function VD_notnull(control,errmsg) {
    return VD_validate((control.value != ''),control,errmsg);
}

function VM_validateonfocus() {
    if (_isError) {
        _isError = false;
        _isLoop = false;
        return;
    }

    var _refactor = false;
    var control;
    ctl = this;
    vt = ctl.value.trim()
    ctl.value = vt;
    var myEvent = getEvent(this.validateobject);
    for (var i=0;i<VAP.length;i++) {
        var V = VAP[i];

        if (V == null) {
            continue;
            _refactor = true;
        }

        control = getControl(V);
        if (control == null || control.parentElement == null) {
            VAP[i] = null;
            _refactor = true;
        } else if ( control != ctl ) {
             if (control != null && !control.disabled) {
                 r = VM_validate(V);
                 if (!r) {
                       if (control.tagName == "SELECT") {
                            if (control.value == '' && control.options.length > 0) {
                                control.options[0].selected = true;
                            }
                       } else {
                            control.select();
                       }
                       control.focus();
                       setHorizontalScroll(control);

                       control.validate = true;
                       _isError = true;
                       return false;
                }
            }
         } else {
            _isError = false;
            _isLoop = true;

            if (ctl.focusevent != null) {
                ctl.focusevent();
            }

            if (ctl.type == "text" || ctl.type == "textarea" || ctl.type == "file" || ctl.type == "password") {
                ctl.select();
                ctl.focus();
            }
            setHorizontalScroll(ctl);
            break;
         }
      }

      if (_refactor) {
        refactor();
      }
}

function refactor() {
    var tmp = new Array();
    var V = null;
    for (var i=0, j=0; i<VAP.length; i++) {
        if (VAP[i] != null) {
            V = VAP[i];
            tmp[j++] = V;
            if (V.control != null) {
                V.control.tabIndex = j;
            }
        }
    }
    VAP = tmp;
}

function getTR(control) {
    var tr = control.parentElement;
    while (tr.tagName !="TR") {
        tr = tr.parentElement;
    }
    return tr;
}

function getfieldname(ctrl) {
   for (i=0; i<VAP.length; i++) {
      if (VAP[i].control == ctrl) {
         return VAP[i].fieldname;
      }
   }
}

function VH_setfilter(regExp) {
   this.validateobject.RE = new RegExp(regExp);
}

function VH_setrange(min, max, isIncluded) {
   if (min != null) {
      this.min = min;
   }

   if (max != null) {
      this.max = max;
   }

   if (isIncluded != null) {
      this.include = true;
   }

}

String.prototype.trim=function (){
   return this.replace(/^\s+/,"").replace(/\s+$/,"");
}

function getNextDate() {
   return addDateDays((new Date()),1);
}

function getCurrDate() {
   var dt = new Date();
   var date = dt.getDate();
   if (date < 10) date = "0" + date;
   var month = dt.getMonth();
   if (month<10 ) month = "0" + month ;
   var stDt = date + "/" + month + "/" + dt.getYear();
   return stDt;
}

function addDateDays(dt,days) {
   dt.setTime(dt.getTime() + (days*24*60*60*1000));

   return dt;
}

function removespaces(s) {

   var i,s,r="";

   for (i=0;i<s.length;i++)
      if (s.charAt(i) != ' ')
         r += s.charAt(i);

   return r;
}

function VH_paste() {
    var V = this.validateobject;
    var _pasteValue = valueprediction(this, clipboardData.getData("Text"), false, V.frameName, false);
    var RE = V.RE;
    var myEvent = getEvent(V);

    if (V.datatype == "number") {
        _pasteValue = VH_removeDecimalSep(_pasteValue);
    }
    if (V.minlen != null) {
        var _len = V.maxlen;
        if (V.minlen == _len) {
            if (! ( VD_validate( ((new String(_pasteValue)).length == _len),this,'TEST MSG {0} 105'.replace("{0}", V.fieldname).replace("{1}",_len) ) ) ) {
                myEvent.returnValue = false;
                return;
            }
        }
    }

    if (RE != null) {
        if (!RE.test(_pasteValue)) {
            myEvent.returnValue = false;
            return;
        } else {
            if (V.datatype == "number") {
                if (isZero(V.prec, _pasteValue)) {
                    myEvent.returnValue = false;
                    return false;
                }
                VH_numericFormat(this, _pasteValue);
                if (this.noformat == null ) {
                    VH_formatScale(this);
                }
                myEvent.returnValue = false;
            } else {
                myEvent.returnValue = true;
                this.changed = true;
            }
        }
        return;
    } else {
        if (this.noUpper == null) {
            this.value = _pasteValue.toUpperCase().substr(0,this.maxLength);
            this.changed = true;
            myEvent.returnValue = false;
        } else {
            this.changed = true;
        }
    }
}

function VH_removeDecimalSep(value) {
    if (value == null || value.length  == 0) return;

    var newValue = '';
    var len = value.length;
    var _char = null;
    if (len == null) {
        newValue = value;
    } else {
        for (i=len; --i>=0; ) {
            _char = value.charAt(i);
            if (_char != ',') {
                newValue = _char + newValue;
            }
        }
    }
    return newValue;
}

function VH_numericFormat(ctl, _value) {
    if (_value == null || _value.length  == 0) {
        ctl.value = "";
        return;
    }

    var V = ctl.validateobject;

    var _unit = V.unit;
    var _prec = V.prec + 1;

    var pos = _value.indexOf(".");
    var value = pos == -1 ? _value : _value.substr(0,pos);
    var scale = pos == -1 ? "" : _value.substr(pos);

    if (value.length > _unit) {
        value = value.substr(0,_unit);
    }

    if (scale.length > _prec) {
        scale = scale.substr(0,_prec);
    }

    var newValue = '';
    var len = value.length;

    var _char = null;
    for (i=len, j=0; --i >= 0; ) {
        _char = value.charAt(i);
        newValue = _char + newValue;

        if (++j%3 == 0 && i!=0) {
            newValue = "," + newValue;
        }

    }
    ctl.value = newValue + scale;
}

function VH_notequalcontrol(control){
   var V1 = this.validateobject;
   V1.control3 = control;
   V1.fieldname3 = getfieldname(control);
   this.notequalcontrol = true;

   var V2 = control.validateobject;
   V2.control3 = this;
   V2.fieldname3 = V1.fieldname;
   control.notequalcontrol = true;
}

function VH_nozero() {
    var V = this.validateobject;
    V.nozero = true;
    if (V.prec == 0) {
         V.RE = new RegExp('^(([1-9])([0-9]{0,' + (V.unit - 1) + '}))$');
    }
}

function VH_formatScale(ctl) {
    var V = ctl.validateobject;
    var _value = ctl.value;
    var pos = _value.indexOf(".");
    var value = pos == -1 ? _value : _value.substr(0,pos);
    var scale = pos == -1 ? "" : _value.substr(pos+1);

    for (var i=scale.length; i<V.prec; i++) {
        scale += '0';
    }

    if (V.prec > 0) {
        if (value == "") value = "0";
        ctl.value = value + "." + scale;
    } else {
        ctl.value = value;
    }
}
