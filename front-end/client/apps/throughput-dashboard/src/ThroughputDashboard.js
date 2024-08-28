/**
 * ThroughputDashboard is defined as
 * `<e-throughput-dashboard>`
 *
 * Imperatively create application
 * @example
 * let app = new ThroughputDashboard();
 *
 * Declaratively create application
 * @example
 * <e-throughput-dashboard></e-throughput-dashboard>
 *
 * @extends {App}
 */
import { definition } from '@eui/component';
import { App, html } from '@eui/app';
import style from './throughputDashboard.css';
import '@eui/table';
import { LineChart } from '@eds/vanilla/charts/line-chart/LineChart';

@definition('e-throughput-dashboard', {
  style,
  props: {
    response: { attribute: false },
  },
})





export default class ThroughputDashboard extends App {
  // Uncomment this block to add initialization code
  // constructor() {
  //   super();
  //   // initialize
  // }



  _populatePage(selectedJob) {

    let jobUrl;

    if (selectedJob == 'all-jobs') {
      jobUrl = 'http://localhost:8080/dashboard/';
    } else {
      jobUrl = `http://localhost:8080/dashboard/job?jobName=${selectedJob}`;
    }

    const tableElement = this.shadowRoot.querySelector("eui-table-v0");
    const chartElement = this.shadowRoot.querySelector('#multi-line-chart');

    fetch(jobUrl)
      .then(function (response) {
        return response.json();
      })
      .then(function (json) {
        // POPULATE TABLE
        // remove current rows
        tableElement.data.pop();
        tableElement.data.pop();
        tableElement.data.pop();
        tableElement.data.pop();

        // add new data
        const rowToAdd = { col1: "Average Build Lead Time", col2: json['durationMedianDORA'] };
        const rowToAdd2 = { col1: "Variation in Build Lead Time", col2: json['durationVarianceDORA'] };
        const rowToAdd3 = { col1: "Average Build Interval", col2: json['intervalMedianDORA'] };
        const rowToAdd4 = { col1: "Variation in Build Interval", col2: json['intervalVarianceDORA'] };
        tableElement.data = [...tableElement.data, rowToAdd, rowToAdd2, rowToAdd3, rowToAdd4];

        // POPULATE CHART
        // line chart
        let chartData = {
          "series" : json['series'],
          "common" : json['common'],
        }

        let linechart = new LineChart({
          element: chartElement,
          data: chartData,
          height: 250,
          margin: {
            left: 35,
          },
          y: {
            unit: 'MINS',
          },
          x: {
            tickFormat: '%b %Y'
          }
        });

        linechart.init();

      })
      .catch(function (ex) {
        console.log('parsing failed', ex)
      })

  }

  /**
  * Render the <e-throughput-dashboard> app. This function is called each time a
  * prop changes.
  */
  render() {
    const { EUI } = window;

    const tableColumns = [
      { title: 'Metric', attribute: 'col1' },
      { title: 'Result', attribute: 'col2' },
    ];
    const tableData = [
      { col1: 'Average Build Lead Time', col2: 'na' },
      { col1: 'Variation in Build Lead Time', col2: 'na' },
      { col1: 'Average Build Interval', col2: 'na' },
      { col1: 'Variation in Build Interval', col2: 'na' },
    ];

    const dropdownData = [
      { label: "All Jobs", value: "all-jobs" },
      { label: "eric-oss-ran-topology-adapter_Publish", value: "eric-oss-ran-topology-adapter_Publish" },
      { label: "eric-oss-ran-topology-adapter_PreCodeReview", value: "eric-oss-ran-topology-adapter_PreCodeReview" },
      { label: "eric-oss-enm-discovery-adapter_PreCodeReview", value: "eric-oss-enm-discovery-adapter_PreCodeReview" },
      { label: "eric-oss-enm-model-adapter_Publish", value: "eric-oss-enm-model-adapter_Publish" },
      { label: "eric-oss-enm-model-adapter_PreCodeReview", value: "eric-oss-enm-model-adapter_PreCodeReview" },
      { label: "eric-oss-enm-notification-adapter_Publish", value: "eric-oss-enm-notification-adapter_Publish" },
      { label: "eric-oss-enm-notification-adapter_PreCodeReview", value: "eric-oss-enm-notification-adapter_PreCodeReview" },
    ]


    return html`

    <eui-layout-v0-tile>
      <div slot="content">
        <h1>Build Throughput Indicator</h1>
      </div>
    </eui-layout-v0-tile>
    
    <eui-layout-v0-tile tile-title="Select a Jenkins Job to display metrics">
      <div slot="content">
    
        <eui-base-v0-dropdown label="Select a job" data-type="single" .data=${dropdownData} @eui-dropdown:click="${(event) =>
            //console.log(event.detail.menuItems[0]._prevProps.value)
            this._populatePage(event.detail.menuItems[0]._prevProps.value)
          }">
        </eui-base-v0-dropdown>
      </div>
    </eui-layout-v0-tile>
    
    <eui-layout-v0-tile tile-title="Build Metrics - Table">
      <div slot="content">
        <eui-table-v0 .columns=${tableColumns} .data=${tableData} striped></eui-table-v0>
      </div>
    </eui-layout-v0-tile>
    
    <eui-layout-v0-tile tile-title="Build Metrics - Graph">
      <div slot="content">
        <div id="multi-line-chart"></div>
      </div>
    </eui-layout-v0-tile>
    `;

  }
}

/**
 * Register the component as e-throughput-dashboard.
 * Registration can be done at a later time and with a different name
 * Uncomment the below line to register the App if used outside the container
 */
// ThroughputDashboard.register();
