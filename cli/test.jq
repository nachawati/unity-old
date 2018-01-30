jsoniq version "1.0";

import module namespace math = "http://www.w3.org/2005/xpath-functions/math";

import module namespace a = "http://dgms.io/unity/modules/analytics/core";

declare function local:f($input)
{
	{
		objective: $input.x * 3 + $input.y * 5,
		constraints: $input.x >= 5 and $input.y >= 10
	}
};


a:argmin(local:f#1, {x: {"integer?": null}, y: {"integer?": null} }, "objective", { solver: "cplex" })