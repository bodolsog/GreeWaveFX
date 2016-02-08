/** Contains reference to active contextmenu. */
var activeMenu;

/**
 * Context menu object.
 *
 * @param {Marker} marker    Marker with which this menu is asociated.
 * @param {Overlay} overlay  Overlay on which is displayed.
 * @returns ContextMenu instance
 * @constructor
 */
var ContextMenu = function (marker, overlay) {

    /** Private methods and variables for this ContextMenu instance. */
    var marker = marker;

    /* Generate menu. */
    var menu = document.createElement('nav');
    menu.classList.add('context-menu');
    var ul = document.createElement('ul');
    ul.classList.add('context-menu__items');
    menu.appendChild(ul);
    document.querySelector('#menus').appendChild(menu);

    /* Indicates that menu was generated. */
    var menuGenerated = false;

    /* List of options for this marker. */
    var options = {
        c1w: 'Connect 1 way',
        c2w: 'Commect 2 way',
        ct: 'Set cross type',
        del: 'Delete marker'
    };

    /** Common methods and variables for all ContextMenu instances. */
    return {
        overlay: overlay,
        visibleCSSClass: 'context-menu--active',
        menuItemCSSClass: 'context-menu__item',
        isActive: function () {
            return menu.classList.contains(this.visibleCSSClass);
        },
        open: function () {
            if (activeMenu != null)
                if (activeMenu.isActive())
                    activeMenu.close();
            this.calculateMenuPosition();
            if (!menuGenerated)
                this.generateMenu();
            menu.classList.add(this.visibleCSSClass); // show menu
            activeMenu = this;
        },
        close: function () {
            menu.classList.remove(this.visibleCSSClass);
        },
        toggle: function () {
            if (this.isActive())
                this.close();
            else
                this.open();
        },
        calculateMenuPosition: function () {
            var point = this.overlay.getProjection().fromLatLngToContainerPixel(marker.getPosition());
            menu.style.left = point.x + "px";
            menu.style.top = point.y + "px";
        },
        generateMenu: function () {
            for (key in options) {
                if (options[key] != null)
                    this.generateOption(key, options[key]);
                menuGenerated = true;
            }
        },
        generateOption: function (key, text) {
            var li = document.createElement('li');
            li.classList.add(this.menuItemCSSClass);
            li.innerHTML = text;
            li.addEventListener('click', function () {
                switch (key) {
                    case 'del':
                        deleteMarker(marker);
                        break;
                    case 'c1w':
                        connectMarkers(marker, false);
                        break;
                    case 'c2w':
                        connectMarkers(marker, true);
                        break;
                }
                menu.classList.remove('context-menu--active');
            });
            li.addEventListener('mouseover', function () {
                this.style.cursor = "pointer";
                this.classList.add('menuItemHover');
            });
            li.addEventListener('mouseout', function () {
                this.classList.remove('menuItemHover');
            });

            ul.appendChild(li);
        },
        removeMenu: function () {
            menu.remove();
        }
    };
};