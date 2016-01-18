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
    gulp.src('./dist/*')
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
  gulp.src('./app/**/*.html')
    .pipe(gulp.dest('dist/'));
});

gulp.task('connect', function () {
  connect.server({
    root: 'app/',
    port: 8888,
    livereload: true
  });
});

gulp.task('html:app', function() {
  return gulp.src('./app/index.html')
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
    .pipe(gulp.dest( './app'));
});

gulp.task('wiredep:app', function() {
  return gulp.src('./app/index.html')
    .pipe(wiredep({
      ignorePath: '../../',
      exclude: [
        './app/bower_components' + '/bootstrap-sass/assets/javascripts/bootstrap.js'
      ]
    }))
    .pipe(gulp.dest('./app'));
});

//Se supone que el index casi no lo voy a modificar
gulp.task('html', function () {
  gulp.src('./app/partials/*.html')
    .pipe(connect.reload());
  gulp.src('./app/partials/**/*.html')
  .pipe(connect.reload());
});

gulp.task('watch', function () {
  gulp.watch(['./app/partials/*.html'], ['html']);
  gulp.watch(['./app/partials/**/*.html'], ['html']);
});

// default task
gulp.task('default',
  ['lint','wiredep:app', 'html:app', 'connect', 'watch']
);
gulp.task('build', function() {
  runSequence(
    ['clean'], ['wiredep:app'],
    ['lint'], ['html:app'], ['minify-css', 'minify-js', 'copy-html-files', 'copy-bower-components', 'connect', 'watch']
  );
});