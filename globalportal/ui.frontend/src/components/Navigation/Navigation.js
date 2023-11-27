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
  const customStyle = !isSubMenuOpen
    ? {
        position: "absolute",
        minWidth:
          item.title === "About Us"
            ? "980px"
            : item.title === "Technology + Intelligence" ||
              item.title === "Technology Intelligence"
            ? "630px"
            : "859px",
      }
    : undefined;
  return (
    <div
      className={`${
        isSubMenuOpen
          ? "cmp-navigation__dropdown__mobile"
          : "cmp-navigation__dropdown"
      }`}
      style={customStyle}
    >
      <div className="cmp-navigation__featured-image-title">
        <div className="cmp-image" id="nav-dropdown-image">
        <Link
            to={item?.link?.url} >
          <img
            className="cmp-image__image"
            src={featuredImage[index].image}
            alt={item.title}
            title={item.title}
          />
          </Link>
        </div>
        <Link
            to={item?.link?.url} ><span id="nav-dropdown-title">{item.title}</span></Link>
      </div>
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

  useEffect(() => {
    const element = document.querySelector(".cmp-homepage__header");
    if (element) {
      if (isOpen) {
        element.classList.add("background");
      } else {
        element.classList.remove("background");
      }
    }
    const headerElement = document.querySelector(".cmp-header--main");
    
    if(headerElement != null){
       if (isOpen) {
          headerElement.classList.remove("header-below-background-color");
       } else {
         headerElement.classList.add("header-below-background-color")
       }
    }
    setTimeout(() => {

      const homeCont = document.getElementsByClassName("home-page-container");

      const heroBanner = document.getElementsByClassName("cmp-herobanner");
      const breadCont = document.getElementsByClassName("bread");
      const centerCont = document.getElementsByClassName(
        "cmp-container--centered"
      );
      const footerCont = document.getElementsByClassName(
        "cmp-footer-container-bg"
      );
      
      if (homeCont && homeCont.length > 0) {
        homeCont[0].style.display = isOpen ? "none" : "block";
      }else{
        heroBanner[0].style.display = isOpen ? "none" : "grid";
        breadCont[0].style.display = isOpen ? "none" : "grid";
        centerCont[0].style.display = isOpen ? "none" : "block";
        footerCont[0].style.display = isOpen ? "none" : "block";
      }
      
    }, 200);
  }, [isOpen]);
  const renderGroupNav = (children, path) => {
    if (children === null || children.length < 1) {
      return null;
    }
    const displayMobMenu =
      !isOpen && dimensions.width < 1050 ? { display: "none" } : undefined;
    return (
      <ul
        className={`cmp-navigation__group ${
          isOpen ? "mobile-menu__active" : ""
        }`}
        style={displayMobMenu}
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
      <li key={baseCss + "__item-" + index} className={cssClass}>
        {renderLink(item, index)}
        {item.level === 0 && headerId ? (
          isOpen && isSubMenuOpen ? (
            index === currentIndex && (
              <SecondaryLevelNavigation
                item={item}
                index={index}
                renderGroupNav={renderGroupNav}
                isSubMenuOpen={isSubMenuOpen}
                featuredImage={featuredImage}
              />
            )
          ) : (
            <SecondaryLevelNavigation
              item={item}
              index={index}
              renderGroupNav={renderGroupNav}
              isSubMenuOpen={isSubMenuOpen}
              featuredImage={featuredImage}
            />
          )
        ) : (
          renderGroupNav(item.children)
        )}
      </li>
    );
  };
  const handleSubmenu = (i) => {
    setCurrentIndex(i);
    setIsSubMenuOpen(!isSubMenuOpen);
  };
  const renderLink = (item, index) => {
    return (
      <div
        className={`cmp-navigation__item-link  ${
          isSubMenuOpen && index === currentIndex ? "show_border" : ""
        }`}
      >
        {(item?.link?.url).includes("https" || "http") ? (
          <a
            href={item?.link?.url}
            title={item.title}
            aria-current={item.active && "page"}
            onClick={() => {
              setIsOpen(false);
              setIsSubMenuOpen(false);
              document.title = item.title
            }}
            target="_blank"
            rel="noopener noreferrer"
          >
            <span>{item.title}</span>
          </a>
        ) : (
          <Link
            to={item?.link?.url}
            title={item.title}
            aria-current={item.active && "page"}
            onClick={() => {
              setIsOpen(false);
              setIsSubMenuOpen(false);

              document.title = item.title

            }}
          >
            <span>{item.title}</span>
          </Link>
        )}
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
  return (
    <nav className="navigation">
      <div className="cmp-navigation" id={props.id}>
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

                      href={localStorage.getItem("for-investors")}

                    >
                      <span className="cmp-button__text">For Investors</span>
                    </a>
                  </div>
                  <div></div>
                </div>
                <div className="cmp-button--width   aem-GridColumn aem-GridColumn--default--12">
                  <div className="button">
                    <a
                      className="cmp-button cmp-button__action-link"
                      id="select-region"

                      href={localStorage.getItem("select-region")}

                    >
                      <span className="cmp-button__text">Select Region</span>
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
