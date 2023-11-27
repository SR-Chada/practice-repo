import React, { Component } from "react";
import { MapTo } from "@adobe/aem-react-editable-components";

require("./PageSectionListing.scss");

//Define EditConfig object
/**
 * Default Edit configuration for the CustomTextComponent component
 *
 * @type EditConfig
 */
const PageSectionListingComponentEditConfig = {
  emptyLabel: "Page Section Listing",
  isEmpty: function (props) {
    return !props || !props.listitems;
  },
};

//Define the React.js component
/**
 * CustomTextComponent React component
 */
class PageSectionListingComponent extends Component {
  render() {
    return (
      <div>
        <ul className='cmp-pagesectionlisting__items'>
            <> {this.props.listItems.map((item, index) =>
                  <li className='cmp-pagesectionlisting__item' key={index}>
                      <a className='cmp-pagesectionlisting__item-link' href={item.itemLink.attributes.href}>{item.itemLabel}</a>
                  </li>)} </>
             </ul>
      </div>
    );
  }
}

//Map the react component to the AEM component using its sling:resourceType
export default MapTo("globalportal/components/pagesectionlisting")(
  PageSectionListingComponent,
  PageSectionListingComponentEditConfig
);
