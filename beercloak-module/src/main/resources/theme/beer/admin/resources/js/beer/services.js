module.factory('Beer', function($resource) {
    return $resource(authUrl + '/realms/:realm/beer/:beerId', {
        realm : '@realm',
        beerId : '@beerId'
    }, {
        update : {
            method : 'PUT'
        },
        drink : {
            method: 'POST',
            url : authUrl + '/realms/:realm/beer/:beerId/drink',
            isArray: true
        }
    });
});

module.factory('BeerLoader', function(Loader, Beer, $route, $q) {
    return Loader.get(Beer, function() {
        return {
            realm : $route.current.params.realm,
            beerId : $route.current.params.beer
        };
    });
});

module.factory('BeerType', function($resource) {
    return $resource(authUrl + '/realms/:realm/beer/types', {
        realm : '@realm'
    }, {
        update : {
            method : 'PUT'
        }
    });
});

module.factory('BeerTypeLoader', function(Loader, BeerType, $route, $q) {
    return Loader.get(BeerType, function() {
        return {
            realm : $route.current.params.realm
        };
    });
});
