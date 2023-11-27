import { MapTo } from "@adobe/aem-react-editable-components";
import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
require("./SearchResults.scss");

const SearchResultsEditConfig = {
  emptyLabel: "Search Results",
  isEmpty: function (props) {
    return !props;
  },
};
const SearchResults = (props) => {
  const queryParams = new URLSearchParams(window.location.search);
  const searchQuery = queryParams.get("search");
  const searchPageTitle = "Search results for :";
  const searchPageFilterText = "Filtered on";
  const [results, setResults] = useState([]);
  const [allData, setAllData] = useState(null);
  const [showBtn, setShowBtn] = useState(false);
  const fetchData = () => {
    const listLength = results.length > 0 ? results.length : 0;
    let url = `${props?.cqPath}.model.json?search=${searchQuery}`;
    if (listLength > 0 && allData?.queryString === searchQuery) {
      url += `&f=${listLength}`;
    }
    fetch(url)
      .then((res) => res.json())
      .then((json) => {
        setResults(
          results.length > 0 && allData?.queryString === searchQuery
            ? [...results, ...json?.results]
            : json?.results
        );
        setAllData(json);
        setShowBtn(listLength < (json?.totalResultsSize-1));
      });
  };
  useEffect(() => {
    fetchData();
  }, [props?.cqPath, searchQuery]);

  return (
    <>
      <div className="search-results">
        <div className="title">
          <h3
            dangerouslySetInnerHTML={{
              __html:
                searchPageTitle +
                " " +
                (searchQuery || allData?.noKeywordMessage),
            }}
          ></h3>
        </div>
        {/* <div className="filter">{searchPageFilterText}</div> */}
        <div className="content-mapping">
          {results.length > 0 ? (
            results.map((item, i) => {
              let featuredImageFile =
                item?.featuredImage &&
                (!item?.featuredImage?.fileReference
                  ? ""
                  : item?.featuredImage?.fileReference);
              return (
                <>
                  {

                   (item?.link?.url).includes("https" || "http") ? (
                    <a href={item?.link?.url} 
                       target="_blank" >
                      <div className="search-container" key={i}>
                        {item.featuredImage && featuredImageFile ? (
                          <div className="image_container">
                            <img
                              alt={item.title}
                              src={featuredImageFile}
                              className="cmp-image__image"
                            />
                          </div>
                        ) : (
                          <div className="placeholder-image"></div>
                        )}
                        <div className="content_container">
                          <div className="heading">
                            <p className={"under_line"}>{item?.title}</p>
                          </div>
                          <div className="content">
                            <p>{item?.description}</p>
                          </div>
                        </div>
                      </div>
                    </a>
                    
                  ) : (
                    <Link to={item?.link?.url}>
                      <div className="search-container" key={i}>
                        {item.featuredImage && featuredImageFile ? (
                          <div className="image_container">
                            <img
                              alt={item.title}
                              src={featuredImageFile}
                              className="cmp-image__image"
                            />
                          </div>
                        ) : (
                          <div className="placeholder-image"></div>
                        )}
                        <div className="content_container">
                          <div className="heading">
                            <p className={"under_line"}>{item?.title}</p>
                          </div>
                          <div className="content">
                            <p>{item?.description}</p>
                          </div>
                        </div>
                      </div>
                    </Link>
                  )   
                  }
                </>
              );
            })
          ) : (
            <div
              style={{ textAlign: "center" }}
              dangerouslySetInnerHTML={{
                __html: allData?.noResultsMessage || "No Results Found",
              }}
            ></div>
          )}
          {results.length !== 0 && showBtn && (
            <div className="show-more" onClick={() => fetchData()}>
              <p>{allData?.buttonLabel}</p>
            </div>
          )}
        </div>
      </div>
    </>
  );
};
//Map the react component to the AEM component using its sling:resourceType
export default MapTo("globalportal/components/searchresults")(
  SearchResults,
  SearchResultsEditConfig
);
