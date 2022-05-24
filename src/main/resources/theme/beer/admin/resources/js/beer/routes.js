module.config([ '$routeProvider', function($routeProvider) {
    $routeProvider
        .when('/realms/:realm/beers', {
            templateUrl : resourceUrl + '/partials/beer-list.html',
            resolve : {
                realm : function(RealmLoader) {
                    return RealmLoader();
                }
            },
            controller : 'BeerListCtrl'
        })
        .when('/realms/:realm/beers/:beer', {
            templateUrl : resourceUrl + '/partials/beer-detail.html',
            resolve : {
                realm : function(RealmLoader) {
                    return RealmLoader();
                },
                beer : function(BeerLoader) {
                    return BeerLoader();
                }
            },
            controller : 'BeerDetailCtrl'
        })
        .when('/create/beer/:realm', {
            templateUrl : resourceUrl + '/partials/beer-detail.html',
            resolve : {
                realm : function(RealmLoader) {
                    return RealmLoader();
                },
                beer : function() {
                    return {};
                }
            },
            controller : 'BeerDetailCtrl'
        })
        .when('/realms/:realm/users/:user/beer', {
            templateUrl : resourceUrl + '/partials/user-beer.html',
            resolve : {
                realm : function(RealmLoader) {
                    return RealmLoader();
                },
                user : function(UserLoader) {
                    return UserLoader();
                }
            },
            controller : 'UserBeerCtrl'
        });

} ]);
