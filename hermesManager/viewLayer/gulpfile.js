// gulp
var gulp = require('gulp');

// plugins
var connect = require('gulp-connect');
var jshint = require('gulp-jshint');
var uglify = require('gulp-uglify');
var minifyCSS = require('gulp-minify-css');
var clean = require('gulp-clean');
var wiredep = require('wiredep').stream;
var inject = require('gulp-inject');
var runSequence = require('run-sequence');

// tasks
gulp.task('lint', function() {
  gulp.src(['./app/**/*.js', '!./app/bower_components/**'])
    .pipe(jshint())
    .pipe(jshint.reporter('default'))
    .pipe(jshint.reporter('fail'));
});
gulp.task('clean', function() {
	return gulp.src('./dist/*')
      .pipe(clean({force: true}));
});
gulp.task('minify-css', function() {
  var opts = {comments:true,spare:true};
  gulp.src(['./app/**/*.css', '!./app/bower_components/**'])
    .pipe(minifyCSS(opts))
    .pipe(gulp.dest('./dist/'))
});
gulp.task('minify-js', function() {
  gulp.src(['./app/**/*.js', '!./app/bower_components/**'])
    .pipe(uglify({
      // inSourceMap:
      // outSourceMap: "app.js.map"
    }))
    .pipe(gulp.dest('./dist/'))
});
gulp.task('copy-bower-components', function () {
  gulp.src('./app/bower_components/**')
    .pipe(gulp.dest('dist/bower_components'));
});
gulp.task('copy-html-files', function () {
  gulp.src('./app/partials/*.html').pipe(gulp.dest('dist/partials/'));
  gulp.src('./app/partials/**/*.html').pipe(gulp.dest('dist/partials/'));
});

gulp.task('connect', function () {
  connect.server({
    root: 'dist/',
    port: 8888,
    livereload: true
  });
});


gulp.task('wiredep:app', function() {
  return gulp.src('./app/index.html')
    .pipe(wiredep({
      ignorePath: '../../',
      exclude: [
        './app/bower_components' + '/bootstrap-sass/assets/javascripts/bootstrap.js'
      ]
    }))
	.pipe(inject(gulp.src([
	      './app/js/app.js',
	      './app/js/**/*.js',
		  './app/js/*.js',
	      './app/css/*.css'
	    ], {
	      read: false
	    }), {
	      relative: true
	    }))
    .pipe(gulp.dest('./dist'));
});

gulp.task('reload', function () {
	 return gulp.src('./dist/')
	    .pipe(connect.reload());	
});

gulp.task('watch', function () {
  gulp.watch(['./app/partials/*.html'], ['wiredep:app', 'reload']);
  gulp.watch(['./app/partials/**/*.html'], ['copy-html-files', 'reload']);
  gulp.watch(['./app/js/*.js'], ['minify-js', 'reload']);
  gulp.watch(['./app/css/*.css'], ['minify-css', 'reload']);
});

// default task
gulp.task('default',
  ['lint','wiredep:app', 'html:app', 'connect', 'watch']
);

gulp.task('build', function(cb) {
	  runSequence(['clean'], 
			  ['wiredep:app'] ,['lint', 'minify-css', 'minify-js', 'copy-html-files', 'copy-bower-components'], 'connect', 'watch', cb);
	});
