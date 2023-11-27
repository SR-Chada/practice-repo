import React, { Component } from 'react';
import { MapTo } from '@adobe/aem-react-editable-components';
import { Link } from "react-router-dom";
import './Breadcrumb.scss';

import {
    BreadCrumbV2,
    BreadCrumbV2IsEmptyFn
} from "@adobe/aem-core-components-react-base";

const BreadCrumb = (props) => {
    const baseCss = 'cmp-breadcrumb';
    
    return (
        <> <nav class="cmp-breadcrumb"> <ol class="cmp-breadcrumb__list"> {props.items.map((item, index) =>
            <li className={`cmp-breadcrumb__item
            ${(item.active ? ' ' + baseCss + '__item--active' : '')}`} key={index}>
          {(item?.link?.url).includes("https" || "http") ? (
             <a href={item?.link?.url} className="cmp-breadcrumb__item-link" target="_blank">
                <span className="cmp-breadcrumb__item-link-text">{item.title}</span>
              </a>
          ) : (
          <Link to={item?.link?.url} className="cmp-breadcrumb__item-link"
          target={item.link?.attributes?.target} onClick={() => {
            document.title = item.title
          }}>
            <span className="cmp-breadcrumb__item-link-text">{item.title}</span>
          </Link>
        )}
          </li>)} </ol> </nav> </>

    )
}


export default BreadCrumb;

MapTo("globalportal/components/breadcrumb")(BreadCrumb, {
    isEmpty: BreadCrumbV2IsEmptyFn,
});