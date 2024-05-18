
<script>
  var pathsData = [(/training/css/font-awesome.min.css,592),(/training/css/awesome-bootstrap-checkbox.css,591),(/training/css/bootstrap.min.css,591),(/training/css/dataTables.bootstrap.min.css,591),(/training/css/bootstrap-social.css,591),(/training/css/fileinput.min.css,591),(/training/css/bootstrap-select.css,591),(/estudiant/wp-admin/admin-ajax.php,431),(/phpmyadmin/index.php?route=/navigation&ajax_request=1,358),(/training/listPays.php,327)];
  var chartDom = document.getElementById('main');
  var myChart = echarts.init(chartDom);
  var option;
  option = {
      tooltip: {
          trigger: 'axis',
          axisPointer: {            
              type: 'shadow'        
          }
      },
      xAxis: {
          type: 'category',
          data: pathsData.map(item => item._1)
      },
      yAxis: {
          type: 'value'
      },
      series: [{
          data: pathsData.map(item => item._2),
          type: 'bar'
      }]
  };
  if (option && typeof option === 'object') {
      myChart.setOption(option);
  }
</script>

