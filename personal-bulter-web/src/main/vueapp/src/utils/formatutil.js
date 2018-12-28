export default {
  formatCurrency(num) {
    let percent = false;
    if (num.toString().indexOf('%') >= 0){
      percent = true;
      num = num.toString().substr(0,num.toString().length-1);
    }
    num = num.toString().replace(/\$|\,/g,'');
    if(isNaN(num))
      num = "0";
    var sign = (num == (num = Math.abs(num)));
    num = Math.floor(num*100+0.50000000001);
    var cents = num%100;
    num = Math.floor(num/100).toString();
    if(cents<10)
      cents = "0" + cents;
    for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)
      num = num.substring(0,num.length-(4*i+3))+','+
        num.substring(num.length-(4*i+3));
    if (percent){
      return (((sign)?'':'-') + num + '.' + cents) + '%';
    }else {
      return (((sign)?'':'-') + num + '.' + cents);
    }
  }
}
