function getInterval(router){
    if(router._lastRouteResolved.query){
        var query = router._lastRouteResolved.query;
        var result = query.split('&');
        return [result[0].replace('begin=', ''), result[1].replace('end=', '')];
    }

    return null;
};