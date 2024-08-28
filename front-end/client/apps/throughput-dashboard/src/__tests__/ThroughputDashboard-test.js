/**
 * Integration tests for <e-throughput-dashboard>
 */
import { expect } from 'chai';
import ThroughputDashboard from '../ThroughputDashboard';
import {
  inShadow,
  injectHTMLElement,
} from '../../../../../test/utils';

describe('ThroughputDashboard Application Tests', () => {
    let container;
    let inject;
    before(() => {
      container = document.body.appendChild(document.createElement('div'));
      inject = injectHTMLElement.bind(null, container);
      window.EUI = undefined; // stub out the locale
      ThroughputDashboard.register();
    });

    after(() => {
      document.body.removeChild(container);
    });

    describe('Basic application setup', () => {
      it('should create a new <e-throughput-dashboard>', async () => {
        const appUnderTest = await inject('<e-throughput-dashboard></e-throughput-dashboard>');
        // check shadow DOM
        const headingTag = inShadow(appUnderTest, 'h1');
        expect(headingTag.textContent, '"Your app markup goes here" was not found').to.equal('Your app markup goes here');
      });
    });
});
