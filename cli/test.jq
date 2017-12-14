jsoniq version "1.0";

import module namespace math = "http://www.w3.org/2005/xpath-functions/math";

declare function local:f($x, $y)
{
	$x + $y
};

local:f(0, 0)