import { MapTo } from "@adobe/aem-react-editable-components";
import React, { useCallback, useEffect, useState } from "react";
import { useHistory } from "react-router-dom";
require("./Search.scss");
const searchEmptyonfig = {
  emptyLabel: "Search component",
  isEmpty: function (props) {
    return true;
  },
};

const Search = (props) => {
  const history = useHistory();
  const [isActive, setisActive] = useState(false);
  const [search, setSearch] = useState("");
  const [dimensions, setDimensions] = React.useState({
    height: window.innerHeight,
    width: window.innerWidth,
  });
  const searchInput = useCallback(
    (inputElement) => {
      if (inputElement && dimensions.width > 1050) {
        isActive && inputElement.focus();
      }
    },
    [isActive]
  );
  useEffect(() => {
    function handleResize() {
      if (window.innerWidth < 1050) {
        setisActive(true);
      } else {
        setisActive(false);
      }
      setDimensions({
        height: window.innerHeight,
        width: window.innerWidth,
      });
    }
    props.isOpen ? setisActive(true) : setisActive(false);
    window.addEventListener("resize", handleResize);
    return (_) => {
      window.removeEventListener("resize", handleResize);
    };
  }, [dimensions]);
  const handleSubmit = (e) => {
    e.preventDefault();
    if (search !== "") {
      history.push(
        `/content/macnicagwi/global/en/search.html?search=${search}`
      );
      if (window.innerWidth < 1050) {
        window.location.reload(false);
      }
      return;
    }
    setisActive(!isActive);
  };

  return (
    <div className="search">
      <div className="cmp-search" id={props.id}>
        <form
          onSubmit={handleSubmit}
          className="cmp-search__form"
          autoComplete="off"
        >
          <div className="cmp-search__field">
            <input
              ref={searchInput}
              className={
                isActive ? "cmp-search__input--active" : "cmp-search__input"
              }
              type="text"
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              name="search"
            />
          </div>
        </form>
      </div>
      <button
        type="submit"
        className="cmp-search--icon"
        onClick={handleSubmit}
      >
        <div id="search__icon"></div>
        <div
          id={isActive ? "search__icon-bg--active" : "search__icon-bg"}
        ></div>
      </button>
    </div>
  );
};

export default MapTo("globalportal/components/search")(
  Search,
  searchEmptyonfig
);
