import React, { useEffect, useRef, useState } from "react";
import { MapTo } from "@adobe/aem-react-editable-components";
import { Link } from "react-router-dom";
import hamburger from "../../assets/icon/hamburger.svg";
import closeIcon from "../../assets/icon/close-icon.svg";
import minusIcon from "../../assets/icon/minus-icon.svg";
import plusIcon from "../../assets/icon/plus-icon.svg";
import Search from "../Search/Search";

const NavigationEditConfig = {
  emptyLabel: "Navigation",
  isEmpty: function (props) {
    return !props || !props.items || props.items.length < 1;
  },
};

/**
 * second level navigation component
 */
const SecondaryLevelNavigation = ({
  renderGroupNav,
  item,
  isSubMenuOpen,
  index,
  featuredImage,
}) => {
  return (
    <div
      className={`${
        isSubMenuOpen
          ? "cmp-navigation__dropdown__mobile"
          : "cmp-navigation__dropdown"
      }`}
    >
      <div className="cmp-navigation__featured-image-title">
        <div className="cmp-image" id="nav-dropdown-image">
          <img
            className="cmp-image__image"
            src={featuredImage[index].image}
            alt={item.title}
            title={item.title}
          />
        </div>
        <span id="nav-dropdown-title">{item.title}</span>
      </div>
      <div className="secondary-level-link">
        {renderGroupNav(item.children, item.path)}
      </div>
    </div>
  );
};
const DeskTop = ({ renderGroupNav, item, index, featuredImage }) => {
  return (
    <div className="cmp-navigation__dropdown drop-desktop">
      {item.level === 0 && <div className="cmp-navigation__featured-image-title">
        <div className="cmp-image" id="nav-dropdown-image">
          <img
            className="cmp-image__image"
            src={featuredImage[index].image}
            alt={item.title}
            title={item.title}
          />
        </div>
        <span id="nav-dropdown-title">{item.title}</span>
      </div>}
      <div className="secondary-level-link">
        {renderGroupNav(item.children, item.path)}
      </div>
    </div>
  );
};

/**
 * Navigation component
 */

const Navigation = (props) => {
  const baseCss = "cmp-navigation";
  const hamBurgerMenuBtn = useRef(null);
  const [isOpen, setIsOpen] = useState(false);
  const [isSubMenuOpen, setIsSubMenuOpen] = useState(false);
  const [currentIndex, setCurrentIndex] = useState(0);
  const headerId = props.id === "header-nav";
  var featuredImage = props.featuredImage;
  const [dimensions, setDimensions] = React.useState({
    height: window.innerHeight,
    width: window.innerWidth,
  });
  useEffect(() => {
    function handleResize() {
      if (window.innerWidth > 1050) {
        setIsOpen(false);
        setIsSubMenuOpen(false);
      }
      setDimensions({
        height: window.innerHeight,
        width: window.innerWidth,
      });
    }
    window.addEventListener("resize", handleResize);
    return (_) => {
      window.removeEventListener("resize", handleResize);
    };
  }, [dimensions]);
  const renderGroupNav = (children, path) => {
    if (children === null || children.length < 1) {
      return null;
    }
    return (
      <ul
        className={`cmp-navigation__group ${
          isOpen ? "mobile-menu__active" : ""
        }`}
      >
        {children.map((item, index) => renderNavItem(item, index))}
      </ul>
    );
  };
  const renderNavItem = (item, index) => {
    const cssClass =
      "cmp-navigation__item " +
      baseCss +
      "__item--level-" +
      item.level +
      " " +
      (isOpen ? baseCss + "__item--mobile " : "") +
      (item.active ? " " + baseCss + "__item--active" : "");
    return (
      <>
        <li key={baseCss + "__item-" + index} className={cssClass}>
          {renderLink(item, index)}
          {item.level === 0 && headerId
            ? isOpen && isSubMenuOpen
              ? index === currentIndex && (
                  <SecondaryLevelNavigation
                    item={item}
                    index={index}
                    renderGroupNav={renderGroupNav}
                    isSubMenuOpen={isSubMenuOpen}
                    featuredImage={featuredImage}
                  />
                )
              : // <SecondaryLevelNavigation
                //   item={item}
                //   index={index}
                //   renderGroupNav={renderGroupNav}
                //   isSubMenuOpen={isSubMenuOpen}
                //   featuredImage={featuredImage}
                // />
                null
            : renderGroupNav(item.children)}
        </li>
        <DeskTop
          item={item}
          index={index}
          renderGroupNav={renderGroupNav}
          featuredImage={featuredImage}
        />
      </>
    );
  };
  const handleSubmenu = (i) => {
    setCurrentIndex(i);
    setIsSubMenuOpen(!isSubMenuOpen);
  };
  const renderLink = (item, index) => {
    return (
      <div className="cmp-navigation__item-link">
        <Link
          to={item?.link?.url}
          title={item.title}
          aria-current={item.active && "page"}
          onClick={() => {
            setIsOpen(false);
            setIsSubMenuOpen(false);
          }}
        >
          <span>{item.title}</span>
        </Link>
        <span className="sub-menu" onClick={() => handleSubmenu(index)}>
          {item.level < 1 && headerId && (
            <span>
              {isSubMenuOpen && index === currentIndex ? (
                <img src={minusIcon} alt="collapse" />
              ) : (
                <img src={plusIcon} alt="expand" />
              )}
            </span>
          )}
        </span>
      </div>
    );
  };
  if (NavigationEditConfig.isEmpty(props)) {
    return null;
  }
  const handleSearchCompState = () => {
    const element = document.getElementsByClassName(
      "cmp-search__input--active"
    );
    const iconElement = document.getElementById("search__icon-bg--active");
    if (element.length > 0) {
      element[0].classList.replace(
        "cmp-search__input--active",
        "cmp-search__input"
      );
      iconElement.id = "search__icon-bg";
    }
  };
  return (
    <nav className="navigation" onMouseOver={handleSearchCompState}>
      <div className="cmp-navigation" id={props.id} >
        <div
          ref={hamBurgerMenuBtn}
          onClick={() => setIsOpen(!isOpen)}
          className="mobile-menu"
          style={{ display: "none" }}
        >
          <img src={isOpen ? closeIcon : hamburger} alt="mobile menu" />
        </div>
        {renderGroupNav(props.items)}
      </div>
      {isOpen && (
        <div className="nav-mobile-buttons cmp-header--buttons   aem-GridColumn aem-GridColumn--default--12">
          <div className="container responsivegrid">
            <div className="cmp-container">
              <div className="nav-mobile-flex aem-container aem-Grid aem-Grid--12 aem-Grid--default--12">
                <div className="cmp-button--width   aem-GridColumn aem-GridColumn--default--12">
                  <div className="button">
                    <a
                      className="cmp-button cmp-button__action-link"
                      id="for-investors"
                      href="https://holdings.macnica.co.jp/ir/en/Top.html"
                    >
                      <span className="cmp-button__text"> For Investors</span>
                    </a>
                  </div>
                  <div></div>
                </div>
                <div className="cmp-button--width   aem-GridColumn aem-GridColumn--default--12">
                  <div className="button">
                    <a
                      className="cmp-button cmp-button__action-link"
                      id="select-region"
                      href="https://www.macnica.com/global/web/"
                    >
                      <span className="cmp-button__text"> Select Region</span>
                    </a>
                  </div>
                  <div></div>
                </div>
              </div>
              <div className="cmp-search--fit-content   aem-GridColumn aem-GridColumn--default--12">
                <Search isOpen={isOpen} />
              </div>
            </div>
          </div>
        </div>
      )}
    </nav>
  );
};

export default Navigation;
MapTo("globalportal/components/navigation")(Navigation, NavigationEditConfig);
