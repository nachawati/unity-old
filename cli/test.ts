declare function variable(name);

interface MfgProcess
{
  constraints(): boolean;
}

class MfgProcess1 implements MfgProcess
{
  cost: number = 9;

  constraints(): boolean {
    return this.cost < 10 + 4;
  }
}

class MfgProcess2 extends MfgProcess1
{
 cost: number = variable("cost")+4;  
}

var p = new MfgProcess2();
var q = variable("cost");
for (var i = 0; i < 100000; i = i + 1)
  q = q + 1; 
q;