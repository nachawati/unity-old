<%@page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<t:page title="${it.workspace.nameWithNamespace}">
	<jsp:attribute name="path">
		<t:workspace.path></t:workspace.path>
	</jsp:attribute>
	<jsp:attribute name="sidebar">
		<t:workspace.sidebar active="home/"></t:workspace.sidebar>
	</jsp:attribute>
	<jsp:attribute name="scripts">
	<script src="${contextPath}/assets/bower_components/chart.js/Chart.js"></script>
	
		<script
			src="${contextPath}/assets/bower_components/raphael/raphael.min.js"></script>
<script
			src="${contextPath}/assets/bower_components/morris.js/morris.min.js"></script>
			
<script
			src="${contextPath}/assets/bower_components/admin-lte/plugins/jvectormap/jquery-jvectormap-1.2.2.min.js"></script>
<script
			src="${contextPath}/assets/bower_components/admin-lte/plugins/jvectormap/jquery-jvectormap-world-mill-en.js"></script>
			<script>
				$(function() {

					// jvectormap data
					var visitorsData = {
						US : 398, // USA
						SA : 400, // Saudi Arabia
						CA : 1000, // Canada
						DE : 500, // Germany
						FR : 760, // France
						CN : 300, // China
						AU : 700, // Australia
						BR : 600, // Brazil
						IN : 800, // India
						GB : 320, // Great Britain
						RU : 3000
					// Russia
					};
					// World map by jvectormap
					$('#world-map')
							.vectorMap(
									{
										map : 'world_mill_en',
										backgroundColor : 'transparent',
										regionStyle : {
											initial : {
												fill : '#e4e4e4',
												'fill-opacity' : 1,
												stroke : 'none',
												'stroke-width' : 0,
												'stroke-opacity' : 1
											}
										},
										series : {
											regions : [ {
												values : visitorsData,
												scale : [ '#92c1dc', '#ebf4f9' ],
												normalizeFunction : 'polynomial'
											} ]
										},
										onRegionLabelShow : function(e, el,
												code) {
											if (typeof visitorsData[code] != 'undefined')
												el.html(el.html() + ': '
														+ visitorsData[code]
														+ ' new visitors');
										}
									});
					
					 /* Morris.js Charts */
					  // Sales chart
					  var area = new Morris.Area({
					    element   : 'revenue-chart',
					    resize    : true,
					    data      : [
					      { y: '2011 Q1', item1: 2666, item2: 2666 },
					      { y: '2011 Q2', item1: 2778, item2: 2294 },
					      { y: '2011 Q3', item1: 4912, item2: 1969 },
					      { y: '2011 Q4', item1: 3767, item2: 3597 },
					      { y: '2012 Q1', item1: 6810, item2: 1914 },
					      { y: '2012 Q2', item1: 5670, item2: 4293 },
					      { y: '2012 Q3', item1: 4820, item2: 3795 },
					      { y: '2012 Q4', item1: 15073, item2: 5967 },
					      { y: '2013 Q1', item1: 10687, item2: 4460 },
					      { y: '2013 Q2', item1: 8432, item2: 5713 }
					    ],
					    xkey      : 'y',
					    ykeys     : ['item1', 'item2'],
					    labels    : ['Item 1', 'Item 2'],
					    lineColors: ['#a0d0e0', '#3c8dbc'],
					    hideHover : 'auto'
					  });
					  
					  
					  
					  
					  
					  
					  
					  
					  
					  
					  
					  

					  // Get context with jQuery - using jQuery's .get() method.
					  var salesChartCanvas = $('#salesChart').get(0).getContext('2d');
					  // This will get the first returned node in the jQuery collection.
					  var salesChart       = new Chart(salesChartCanvas);

					  var salesChartData = {
					    labels  : ['January', 'February', 'March', 'April', 'May', 'June', 'July'],
					    datasets: [
					      {
					        label               : 'Electronics',
					        fillColor           : 'rgb(210, 214, 222)',
					        strokeColor         : 'rgb(210, 214, 222)',
					        pointColor          : 'rgb(210, 214, 222)',
					        pointStrokeColor    : '#c1c7d1',
					        pointHighlightFill  : '#fff',
					        pointHighlightStroke: 'rgb(220,220,220)',
					        data                : [65, 59, 80, 81, 56, 55, 40]
					      },
					      {
					        label               : 'Digital Goods',
					        fillColor           : 'rgba(60,141,188,0.9)',
					        strokeColor         : 'rgba(60,141,188,0.8)',
					        pointColor          : '#3b8bba',
					        pointStrokeColor    : 'rgba(60,141,188,1)',
					        pointHighlightFill  : '#fff',
					        pointHighlightStroke: 'rgba(60,141,188,1)',
					        data                : [28, 48, 40, 19, 86, 27, 90]
					      }
					    ]
					  };

					  var salesChartOptions = {
					    // Boolean - If we should show the scale at all
					    showScale               : true,
					    // Boolean - Whether grid lines are shown across the chart
					    scaleShowGridLines      : false,
					    // String - Colour of the grid lines
					    scaleGridLineColor      : 'rgba(0,0,0,.05)',
					    // Number - Width of the grid lines
					    scaleGridLineWidth      : 1,
					    // Boolean - Whether to show horizontal lines (except X axis)
					    scaleShowHorizontalLines: true,
					    // Boolean - Whether to show vertical lines (except Y axis)
					    scaleShowVerticalLines  : true,
					    // Boolean - Whether the line is curved between points
					    bezierCurve             : true,
					    // Number - Tension of the bezier curve between points
					    bezierCurveTension      : 0.3,
					    // Boolean - Whether to show a dot for each point
					    pointDot                : false,
					    // Number - Radius of each point dot in pixels
					    pointDotRadius          : 4,
					    // Number - Pixel width of point dot stroke
					    pointDotStrokeWidth     : 1,
					    // Number - amount extra to add to the radius to cater for hit detection outside the drawn point
					    pointHitDetectionRadius : 20,
					    // Boolean - Whether to show a stroke for datasets
					    datasetStroke           : true,
					    // Number - Pixel width of dataset stroke
					    datasetStrokeWidth      : 2,
					    // Boolean - Whether to fill the dataset with a color
					    datasetFill             : true,
					    // String - A legend template
					    // Boolean - whether to maintain the starting aspect ratio or not when responsive, if set to false, will take up entire container
					    maintainAspectRatio     : true,
					    // Boolean - whether to make the chart responsive to window resizing
					    responsive              : true
					  };

					  // Create the line chart
					  salesChart.Line(salesChartData, salesChartOptions);


				});
			</script>
	</jsp:attribute>
	<jsp:body>
	<div class="row">
        <div class="col-lg-3 col-xs-6">
          <!-- small box -->
          <div class="small-box bg-aqua">
            <div class="inner">
              <h3>150</h3>

              <p>New Orders</p>
            </div>
            <div class="icon">
              <i class="ion ion-bag"></i>
            </div>
            <a href="#" class="small-box-footer">More info <i
						class="fa fa-arrow-circle-right"></i></a>
          </div>
        </div>
        <!-- ./col -->
        <div class="col-lg-3 col-xs-6">
          <!-- small box -->
          <div class="small-box bg-green">
            <div class="inner">
              <h3>53<sup style="font-size: 20px">%</sup>
						</h3>

              <p>Bounce Rate</p>
            </div>
            <div class="icon">
              <i class="ion ion-stats-bars"></i>
            </div>
            <a href="#" class="small-box-footer">More info <i
						class="fa fa-arrow-circle-right"></i></a>
          </div>
        </div>
        <!-- ./col -->
        <div class="col-lg-3 col-xs-6">
          <!-- small box -->
          <div class="small-box bg-yellow">
            <div class="inner">
              <h3>44</h3>

              <p>User Registrations</p>
            </div>
            <div class="icon">
              <i class="ion ion-person-add"></i>
            </div>
            <a href="#" class="small-box-footer">More info <i
						class="fa fa-arrow-circle-right"></i></a>
          </div>
        </div>
        <!-- ./col -->
        <div class="col-lg-3 col-xs-6">
          <!-- small box -->
          <div class="small-box bg-red">
            <div class="inner">
              <h3>65</h3>

              <p>Unique Visitors</p>
            </div>
            <div class="icon">
              <i class="ion ion-pie-graph"></i>
            </div>
            <a href="#" class="small-box-footer">More info <i
						class="fa fa-arrow-circle-right"></i></a>
          </div>
        </div>
        <!-- ./col -->
      </div>
      <!-- /.row -->
      <!-- Main row -->
      
      <div class="row">
      	<div class="col-lg-12">
      	<div class="box">
            <div class="box-header with-border">
              <h3 class="box-title">Monthly Recap Report</h3>

              <div class="box-tools pull-right">
                <button type="button" class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i>
                </button>
                <div class="btn-group">
                  <button type="button" class="btn btn-box-tool dropdown-toggle" data-toggle="dropdown">
                    <i class="fa fa-wrench"></i></button>
                  <ul class="dropdown-menu" role="menu">
                    <li><a href="#">Action</a></li>
                    <li><a href="#">Another action</a></li>
                    <li><a href="#">Something else here</a></li>
                    <li class="divider"></li>
                    <li><a href="#">Separated link</a></li>
                  </ul>
                </div>
                <button type="button" class="btn btn-box-tool" data-widget="remove"><i class="fa fa-times"></i></button>
              </div>
            </div>
            <!-- /.box-header -->
            <div class="box-body">
              <div class="row">
                <div class="col-md-8">
                  <p class="text-center">
                    <strong>Sales: 1 Jan, 2014 - 30 Jul, 2014</strong>
                  </p>

                  <div class="chart">
                    <!-- Sales Chart Canvas -->
                    <canvas id="salesChart" style="height: 129px; width: 589px;" width="736" height="161"></canvas>
                  </div>
                  <!-- /.chart-responsive -->
                </div>
                <!-- /.col -->
                <div class="col-md-4">
                  <p class="text-center">
                    <strong>Goal Completion</strong>
                  </p>

                  <div class="progress-group">
                    <span class="progress-text">Add Products to Cart</span>
                    <span class="progress-number"><b>160</b>/200</span>

                    <div class="progress sm">
                      <div class="progress-bar progress-bar-aqua" style="width: 80%"></div>
                    </div>
                  </div>
                  <!-- /.progress-group -->
                  <div class="progress-group">
                    <span class="progress-text">Complete Purchase</span>
                    <span class="progress-number"><b>310</b>/400</span>

                    <div class="progress sm">
                      <div class="progress-bar progress-bar-red" style="width: 80%"></div>
                    </div>
                  </div>
                  <!-- /.progress-group -->
                  <div class="progress-group">
                    <span class="progress-text">Visit Premium Page</span>
                    <span class="progress-number"><b>480</b>/800</span>

                    <div class="progress sm">
                      <div class="progress-bar progress-bar-green" style="width: 80%"></div>
                    </div>
                  </div>
                  <!-- /.progress-group -->
                  <div class="progress-group">
                    <span class="progress-text">Send Inquiries</span>
                    <span class="progress-number"><b>250</b>/500</span>

                    <div class="progress sm">
                      <div class="progress-bar progress-bar-yellow" style="width: 80%"></div>
                    </div>
                  </div>
                  <!-- /.progress-group -->
                </div>
                <!-- /.col -->
              </div>
              <!-- /.row -->
            </div>
            <!-- ./box-body -->
            <div class="box-footer">
              <div class="row">
                <div class="col-sm-3 col-xs-6">
                  <div class="description-block border-right">
                    <span class="description-percentage text-green"><i class="fa fa-caret-up"></i> 17%</span>
                    <h5 class="description-header">$35,210.43</h5>
                    <span class="description-text">TOTAL REVENUE</span>
                  </div>
                  <!-- /.description-block -->
                </div>
                <!-- /.col -->
                <div class="col-sm-3 col-xs-6">
                  <div class="description-block border-right">
                    <span class="description-percentage text-yellow"><i class="fa fa-caret-left"></i> 0%</span>
                    <h5 class="description-header">$10,390.90</h5>
                    <span class="description-text">TOTAL COST</span>
                  </div>
                  <!-- /.description-block -->
                </div>
                <!-- /.col -->
                <div class="col-sm-3 col-xs-6">
                  <div class="description-block border-right">
                    <span class="description-percentage text-green"><i class="fa fa-caret-up"></i> 20%</span>
                    <h5 class="description-header">$24,813.53</h5>
                    <span class="description-text">TOTAL PROFIT</span>
                  </div>
                  <!-- /.description-block -->
                </div>
                <!-- /.col -->
                <div class="col-sm-3 col-xs-6">
                  <div class="description-block">
                    <span class="description-percentage text-red"><i class="fa fa-caret-down"></i> 18%</span>
                    <h5 class="description-header">1200</h5>
                    <span class="description-text">GOAL COMPLETIONS</span>
                  </div>
                  <!-- /.description-block -->
                </div>
              </div>
              <!-- /.row -->
            </div>
            <!-- /.box-footer -->
          </div>
      	</div>
      </div>
      <div class="row">
        <!-- Left col -->
        <section class="col-lg-7 connectedSortable">
          <!-- Custom tabs (Charts with tabs)-->
          <div class="nav-tabs-custom">
            <!-- Tabs within a box -->
            <ul class="nav nav-tabs pull-right">
              <li class="active"><a href="#revenue-chart"
							data-toggle="tab">Area</a></li>
              <li class="pull-left header"><i class="fa fa-inbox"></i> Sales</li>
            </ul>
            <div class="tab-content no-padding">
              <!-- Morris chart - Sales -->
              <div class="chart tab-pane active" id="revenue-chart"
							style="position: relative; height: 300px;"></div>
              
            </div>
          </div>
          <!-- /.nav-tabs-custom -->

          

           </section>
        <!-- /.Left col -->
        <!-- right col (We are only adding the ID to make the widgets sortable)-->
        <section class="col-lg-5 connectedSortable">

          <!-- Map box -->
          <div class="box box-solid bg-light-blue-gradient">
            <div class="box-header">
              <!-- tools box -->
              <div class="pull-right box-tools">
                <button type="button"
								class="btn btn-primary btn-sm daterange pull-right"
								data-toggle="tooltip" title="Date range">
                  <i class="fa fa-calendar"></i>
							</button>
                <button type="button"
								class="btn btn-primary btn-sm pull-right" data-widget="collapse"
								data-toggle="tooltip" title="Collapse"
								style="margin-right: 5px;">
                  <i class="fa fa-minus"></i>
							</button>
              </div>
              <!-- /. tools -->

              <i class="fa fa-map-marker"></i>

              <h3 class="box-title">
                Visitors
              </h3>
            </div>
            <div class="box-body">
              <div id="world-map" style="height: 250px; width: 100%;"></div>
            </div>
            <!-- /.box-body-->
            <div class="box-footer no-border">
              <div class="row">
                <div class="col-xs-4 text-center"
								style="border-right: 1px solid #f4f4f4">
                  <div id="sparkline-1"></div>
                  <div class="knob-label">Visitors</div>
                </div>
                <!-- ./col -->
                <div class="col-xs-4 text-center"
								style="border-right: 1px solid #f4f4f4">
                  <div id="sparkline-2"></div>
                  <div class="knob-label">Online</div>
                </div>
                <!-- ./col -->
                <div class="col-xs-4 text-center">
                  <div id="sparkline-3"></div>
                  <div class="knob-label">Exists</div>
                </div>
                <!-- ./col -->
              </div>
              <!-- /.row -->
            </div>
          </div>
          <!-- /.box -->


        </section>
        <!-- right col -->
      </div>
      <!-- /.row (main row) -->
	</jsp:body>
</t:page>
