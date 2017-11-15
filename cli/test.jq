jsoniq version "1.0";

import module namespace sym = "http://dgms.io/unity/modules/symbolics";
import module namespace diff = "http://dgms.io/unity/modules/analysis/differentiation";

let $x := sym:variable('x')
let $e := $x * $x
return diff:differentiate($e, $x)
