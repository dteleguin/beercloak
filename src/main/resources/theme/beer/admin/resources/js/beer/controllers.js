module.controller('BeerTabCtrl', function($scope, $location, Dialog, Notifications, Current) {
    $scope.removeBeer = function() {
        Dialog.confirmDelete($scope.beer.name, 'beer', function() {
            $scope.beer.$remove({
                realm : Current.realm.realm,
                beerId : $scope.beer.id
            }, function() {
                $location.url("/realms/" + Current.realm.realm + "/beers");
                Notifications.success("Beer has been deleted.");
            }, function() {
                Notifications.error("Beer couldn't be deleted");
            });
        });
    };
});

module.controller('BeerListCtrl', function($scope, realm, Beer, Notifications, $route, Dialog) {

    $scope.realm = realm;
    $scope.page = 0;

    $scope.query = {
        realm: realm.realm,
        max: 20,
        first: 0
    };

    $scope.firstPage = function() {
        $scope.query.first = 0;
        $scope.searchQuery();
    };

    $scope.previousPage = function() {
        $scope.query.first -= parseInt($scope.query.max);
        if ($scope.query.first < 0) {
            $scope.query.first = 0;
        }
        $scope.searchQuery();
    };

    $scope.nextPage = function() {
        $scope.query.first += parseInt($scope.query.max);
        $scope.searchQuery();
    };

    $scope.searchQuery = function() {
        $scope.searchLoaded = false;

        $scope.beers = Beer.query($scope.query, function() {
            $scope.searchLoaded = true;
            $scope.lastSearch = $scope.query.search;
        });
    };

    $scope.removeBeer = function(beer) {
        Dialog.confirmDelete(beer.name, 'beer', function() {
            beer.$remove({
                realm : realm.realm,
                beerId : beer.id
            }, function() {
                $route.reload();
                Notifications.success("Beer has been deleted.");
            }, function() {
                Notifications.error("Beer couldn't be deleted");
            });
        });
    };

});

module.controller('BeerDetailCtrl', function($modal, $scope, realm, beer, Beer, BeerType,
                                             $location, $route, Dialog, Notifications) {

    $scope.realm = realm;
    $scope.create = !beer.id;
    $scope.typesLoaded = false;
    $scope.qty = 1;

    $scope.types = BeerType.query({ realm: realm.realm }, function() {
        $scope.typesLoaded = true;
    });

    if ($scope.create) {
        $scope.beer = {};
    } else {
        $scope.beer = angular.copy(beer);
    }

    $scope.changed = false; // $scope.create;

    $scope.$watch('beer', function() {
        if (!angular.equals($scope.beer, beer)) {
            $scope.changed = true;
        }
    }, true);

    $scope.save = function() {

        if ($scope.create) {
            Beer.save({
                realm: realm.realm
            }, $scope.beer, function (data, headers) {
                $scope.changed = false;
                beer = angular.copy($scope.beer);
                var l = headers().location;

                var id = l.substring(l.lastIndexOf("/") + 1);

                $location.url("/realms/" + realm.realm + "/beers/" + id);
                Notifications.success("Beer has been created.");
            });
        } else {
            Beer.update({
                realm: realm.realm,
                beerId: $scope.beer.id
            }, $scope.beer, function () {
                $scope.changed = false;
                beer = angular.copy($scope.beer);
                Notifications.success("Your changes have been saved to beer.");
            });
        }
    };

    $scope.reset = function() {
        $scope.beer = angular.copy(beer);
        $scope.changed = false;
    };

    $scope.cancel = function() {
        $location.url("/realms/" + realm.realm + "/beers");
    };

    $scope.drink = function(qty) {
        Beer.drink({ realm: realm.realm, beerId: beer.id }, [ qty ], function(response) {
            $modal.open({
                templateUrl: resourceUrl + '/partials/modal/gulp.html',
                controller: 'GulpCtrl',
                resolve: {
                    response: function() {
                        return response;
                    }
                }
            });
        });
    };

});

module.controller('UserBeerCtrl', function($scope, realm, user, Beer) {

    $scope.realm = realm;
    $scope.user = user;
    $scope.beersLoaded = false;
    $scope.changed = false;

    $scope.beers = Beer.query({realm: realm.realm}, function () {
        $scope.beersLoaded = true;
    });

    $scope.$watch('beer', function () {
        if (!angular.equals($scope.beer, beer)) {
            $scope.changed = true;
        }
    }, true);

});

module.controller('GulpCtrl', function($scope, response, $modalInstance) {

    $scope.drunk = response[0];

    $scope.cancel = function() {
        $modalInstance.dismiss();
    };

    $scope.ok = function() {
        $modalInstance.close();
    };

});
