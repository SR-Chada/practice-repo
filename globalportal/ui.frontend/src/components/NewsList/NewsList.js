//Imports
import React, { useEffect, useState } from "react";
import { MapTo } from "@adobe/aem-react-editable-components";
import { Button } from "../Button/Button";
import { Link } from "react-router-dom";
import { monthNames } from "../Common/Common";

require("./NewsList.scss");

//Define EditConfig object
/**
 * Default Edit configuration for the CustomTextComponent component
 *
 * @type EditConfig
 */
const NewsListComponentEditConfig = {
  emptyLabel: "News List",
  isEmpty: function (props) {
    return !props;
  },
};

//Define the React.js component
/**
 * CustomTextComponent React component
 */

const LinkData = ({ item, formattedDate }) => {
  return (
    <>
      <div className="info-section">
        <span className="date">{formattedDate}</span>
        {item?.tags &&
          item.tags.map((tag, index) => (
            <span className="category" key={index}>
              {tag}
            </span>
          ))}
      </div>
      <div className="title-section">{item?.pageTitle} </div>
    </>
  );
};

const NewsList = (props) => {
  const [newsList, setNewsList] = useState([]);
  const [loading, setLoading] = useState(false);
  const [totalPages, setTotalPages] = useState([]);
  const [currentPage, setcurrentPage] = useState(1);

  useEffect(() => {
    setLoading(true);
    fetch(`${props?.cqPath}.serv.json?pageNumber=${currentPage}`)
      .then((res) => res.json())
      .then((json) => {
        setNewsList(
          json?.newsList
            ? json?.newsList
            : []
        );
        
        if (json?.totalResults && props?.maxItems) {
          const totalPageCount = Math.ceil(
            json?.totalResults / props?.maxItems
          );
          const pageList = [];

          for (let i = 1; i <= totalPageCount; i++) {
            pageList.push(i);
          }
          setTotalPages(pageList);
        }
        setLoading(false);
      });
  }, [props.cqPath, currentPage]);
  const getButtonLink = (buttonLink) => {
    const temp = {
      url: buttonLink?.pagePath + ".html",
      attributes: {
        target: buttonLink?.pagePath ? buttonLink?.pagePath : "_self",
      },
    };
    return temp;
  };
  return (
    <div className="newslist-container">
      {loading && (
        <div className="loading">
          <div className="spinner"></div>
          <span>Loading...</span>
        </div>
      )}
      <ul className="newslist">
        {!loading &&
          newsList.length > 0 &&
          newsList.map((item, i) => {
            
            const date = new Date(item?.date);
            const day = item?.date && date.getDate();

            const month = item?.date && monthNames[date.getMonth()].slice(0, 3);
            const year = item?.date && date.getFullYear();
            const formattedDate = item?.date ? `${month}. ${day}, ${year}` : "";
           
            return (
              <li className="listitem" key={i}>
                {(item?.link?.url).includes("https" || "http") ? (

                  
                   <a href={item?.link?.url}
                    title={item?.pageTitle}
                    target="_blank">
                        <LinkData item={item} formattedDate={formattedDate} />
                        </a>
                  
                ) : (
                  <Link to={item?.link?.url} title={item?.pageTitle} onClick={() => {
                    document.title = item.pageTitle
                  }}>

                    <LinkData item={item} formattedDate={formattedDate} />
                  </Link>
                )}

                <div className="divider" />
              </li>
            );
          })}
      </ul>
      {props?.pagination !== "false"
        ? !loading && (
            <div className="pagination">
              <div
                className="next-number"
                onClick={() =>
                  setcurrentPage(
                    currentPage == totalPages[0] ? currentPage : currentPage - 1
                  )
                }
              ></div>
              <ul className="page-number-list">
                {totalPages.length > 0 &&
                  totalPages.map((pageNumber, index) => (
                    <li
                      key={index}
                      className={`page-number ${
                        pageNumber === currentPage && "active-page"
                      }`}
                      onClick={() => setcurrentPage(pageNumber)}
                    >
                      <span>{pageNumber}</span>
                    </li>
                  ))}
              </ul>
              <div
                className="prev-number"
                onClick={() =>
                  setcurrentPage(
                    currentPage == totalPages.slice(-1)
                      ? currentPage
                      : currentPage + 1
                  )
                }
              ></div>
            </div>
          )
        : !loading && (
            <div className="cmp-button-inside-arrow">
              <Button
                text={props?.readMoreButtonText}
                buttonLink={getButtonLink(props)}
              />
            </div>
          )}
    </div>
  );
};

//Map the react component to the AEM component using its sling:resourceType
export default MapTo("globalportal/components/newslist")(
  NewsList,
  NewsListComponentEditConfig
);
