var _ = (function () {
    function _() {
    }
    _._and = function (a,b) { return  a && b; };
    _._or = function (a,b) { return a || b; };
    _._if = function (a,b,c) { return a ? b : c; };
    _._add = function (a,b) { return a.symbolic || b.symbolic ? __._add(a,b) : a+b; };
    _._lt = function (a,b) { return a.symbolic || b.symbolic ? __._lt(a,b) : a < b; };
    return _;
}());