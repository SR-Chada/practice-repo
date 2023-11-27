import React, { Component } from "react";
import { MapTo } from "@adobe/aem-react-editable-components";
import extractModelId from "../../utils/extract-model-id";
require("./Table.scss");

export const TableEditConfig = {
  emptyLabel: "Table",

  isEmpty: function (props) {
    return !props || !props.text || props.text.trim().length < 1;
  },
};

export default class Table extends Component {
  constructor(props) {
    super(props);
    this.state = {
      isSwipeActive: false,
    };
    // this.handleOnClick = this.handleOnClick.bind(this);
  }
  get richTextContent() {
    return (
      <div
        className="cmp-table"
        id={extractModelId(this.props.cqPath)}
        data-rte-editelement
        dangerouslySetInnerHTML={{ __html: this.props.text }}
      />
    );
  }

  get textContent() {
    return <div>{this.props.text}</div>;
  }
  componentDidMount() {
    const elem = document.getElementById(extractModelId(this.props.cqPath));
    const tbody = elem?.firstElementChild?.childNodes[1];
    const childNodes = tbody ? tbody?.childNodes : { childNodes: [] };
    if (childNodes?.length > 0 && childNodes[0]?.childElementCount > 2) {
      this.setState({ isSwipeActive: true });
    }
    setTimeout(() => {
      this.setState({ isSwipeActive: false });
    }, 5000);
  }
 
  render() {
    return this.props.richText ? (
      <div className="main-container">
        <div
          className="swipe-circle"
          style={{
            opacity: this.state.isSwipeActive ? 0.75 : 0,
            transition: "all 1s ease-in",
          }}
        >
          SWIPE
        </div>
        <div className="arrow"></div>
        {this.richTextContent}
      </div>
    ) : (
      this.textContent
    );
  }
}

MapTo("globalportal/components/table")(Table, TableEditConfig);
