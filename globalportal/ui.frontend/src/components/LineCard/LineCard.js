//Imports
import React, { useEffect, useState } from "react";
import { MapTo } from "@adobe/aem-react-editable-components";
import { Link } from "react-router-dom";

require("./LineCard.scss");

//Define EditConfig object
/**
 * Default Edit configuration for the CustomTextComponent component
 *
 * @type EditConfig
 */
const LineCardComponentEditConfig = {
  emptyLabel: "Line Card",
  isEmpty: function (props) {
    return !props;
  },
};

//Define the React.js component
/**
 * CustomTextComponent React component
 */
const LineCard = (props) => {
  const [lineCardItems, setLineCardItems] = useState([]);
  useEffect(() => {
    if (props?.itemsMap) {
      const toArray = Object.entries(props?.itemsMap);
      setLineCardItems(toArray);
    }
  }, []);
  
  return (
    <div className="cmp-linecard">
      {lineCardItems.length > 0 &&
        lineCardItems.map((item, i) => (
          <div key={item[0]}>
            <div class="cmp-title--align-center">
              <div class="cmp-title">
                <h1 class="cmp-title__text">
                  <div class="cmp-title__text-content">{item[0]}</div>
                </h1>
              </div>
            </div>
            <ul className="cmp-linecard__items">
              {item[1].length > 0 &&
                item[1].map((item, index) => (
                  <li className="cmp-linecard__item" key={index}>
                    <a href={item?.link} class="cmp-linecard__items-link" target="_blank">
                      <img src={item?.logo} class="cmp-image__image" />
                      <div className="cmp-linecard__items-description">
                        {item?.description}
                      </div>
                    </a>
                  </li>
                ))}
            </ul>
          </div>
        ))}
    </div>
  );
};

//Map the react component to the AEM component using its sling:resourceType
export default MapTo("globalportal/components/linecardlist")(
  LineCard,
  LineCardComponentEditConfig
);
